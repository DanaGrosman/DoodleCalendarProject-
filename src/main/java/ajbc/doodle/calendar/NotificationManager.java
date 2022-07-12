package ajbc.doodle.calendar;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;

@Component
public class NotificationManager implements Runnable {

	@Autowired
	NotificationService notificationService;

	@Autowired
	UserService userService;

	@Autowired
	MessagePushService messagePushService;

	private int numberOfThreads;
	private PriorityQueue<Notification> queue = new PriorityQueue<Notification>();
	private ScheduledThreadPoolExecutor threadPool;

	public NotificationManager() throws DaoException {
		this.queue = new PriorityQueue<Notification>(
				(n1, n2) -> n1.computeAlertTime().compareTo(n2.computeAlertTime()));
	}

	@PostConstruct
	public void initQueue() throws DaoException {
		this.queue.addAll(notificationService.getAllNotifications());
		numberOfThreads = queue.size();
		threadPool = new ScheduledThreadPoolExecutor(numberOfThreads);
		threadPool.schedule(this, LocalDateTime.now().getSecond(), TimeUnit.SECONDS);
	}

	public void addNotification(Notification notification) {
		for (Notification n : (Notification[]) queue.toArray()) {
			if (n.getNotificationId().equals(notification.getNotificationId())) {
				queue.remove(n);
			}
		}

		Notification first = queue.peek();
		queue.add(notification);
		if (first == null || notification.getLocalDateTime().isBefore(first.getLocalDateTime())) {
			LocalDateTime now = LocalDateTime.now();
			long seconds = ChronoUnit.SECONDS.between(now, notification.getLocalDateTime());
			threadPool.schedule(this, seconds, TimeUnit.SECONDS);
		}
	}

	@Override
	public void run() {
		// TODO: assign thread to send the next notification (top queue) if the user is
		// logged in
		// if not- mark the notification as irrelevant, move on to the next notification
		// mark notification as sent if it was sent

		// all notifications that need to be sent now - add to list from queue
		// after collecting all current notifications- open thread pool
		// each thread sends one notification to user
		// set manager to sleep until the next closest notification timing

		List<Notification> notificationsToPush = new ArrayList<Notification>();
		Notification notification = queue.peek();
		Notification current = queue.peek();
//		LocalDateTime alertTime = notification.computeAlertTime();

		try {
			
			while (current.computeAlertTime().equals(notification.computeAlertTime())) {
				queue.poll();

				if (userService.getUserById(current.getUserId()).getIsLogged()) {
					notificationsToPush.add(current);
				}

				current.setAlerted(true);
				notificationService.updateNotification(current);

				current = queue.peek();
			}

			numberOfThreads = notificationsToPush.size();
			threadPool = new ScheduledThreadPoolExecutor(numberOfThreads);
			notificationsToPush.forEach(System.out::println);

			notificationsToPush.forEach(t -> threadPool.execute(new NotificationThread(t, messagePushService)));

			threadPool.schedule(this, getDelay(queue.peek().getLocalDateTime()), TimeUnit.SECONDS);
			threadPool.schedule(this, 3, TimeUnit.SECONDS);

		
		} catch (DaoException e) {
			e.printStackTrace();
		}
		

	}

	private long getDelay(LocalDateTime time) {
		LocalDateTime now = LocalDateTime.now();
		long seconds = ChronoUnit.SECONDS.between(now, time);
		return seconds;
	}
}
