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
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;
import lombok.Getter;

@Getter
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
		this.queue = new PriorityQueue<Notification>((n1, n2) -> n1.getAlertTime().compareTo(n2.getAlertTime()));
	}

	@PostConstruct
	public void initQueue() throws DaoException {
		System.out.println("initQueue() start");
		this.queue.addAll(notificationService.getNotificationsNotAlerted());

		numberOfThreads = queue.size();
		threadPool = new ScheduledThreadPoolExecutor(numberOfThreads);

		schedule();
	}

	public void addNotification(Notification notification) throws DaoException {
		// Check if the notification is already exists
		queue.removeIf(t -> t.getNotificationId().equals(notification.getNotificationId()));

		Notification first = queue.peek();
		queue.add(notification);

		if (first == null || notification.getAlertTime().isBefore(first.getAlertTime())) {
			LocalDateTime now = LocalDateTime.now();
			long seconds = ChronoUnit.SECONDS.between(now, notification.getAlertTime());
			threadPool = new ScheduledThreadPoolExecutor(queue.size());
			threadPool.schedule(this, seconds, TimeUnit.SECONDS);
		}
	}

	@Override
	public void run() {
		// assign thread to send the next notification (top queue) if the user is
		// logged in
		// if not- mark the notification as irrelevant, move on to the next notification
		// mark notification as sent if it was sent

		// all notifications that need to be sent now - add to list from queue
		// after collecting all current notifications- open thread pool
		// each thread sends one notification to user
		// set manager to sleep until the next closest notification timing

		List<Notification> notificationsToPush = new ArrayList<>();
		Notification first = queue.peek();
		Notification current;
		
		if (first != null) {
			do {
				current = queue.poll();
				User user = current.getUser();
				if (user.getIsLogged() && !current.isAlerted()) {
					notificationsToPush.add(current);
				}

				current.setAlerted(true);
				
				try {
					notificationService.updateNotification(current);
				} catch (DaoException e) {
					e.printStackTrace();
				}
				
				current = queue.peek();

			} while (current != null && current.getAlertTime().equals(first.getAlertTime()));

			notificationsToPush.forEach(t -> threadPool.execute(new NotificationThread(t, messagePushService)));
		}

		schedule();

	}

	long getDelay(LocalDateTime time) {
		return ChronoUnit.SECONDS.between(LocalDateTime.now(), time);
	}

	private void schedule() {
		if (queue.peek() != null) {
			if (queue.peek().getAlertTime().isBefore(LocalDateTime.now()))
				threadPool.schedule(this, 0, TimeUnit.SECONDS);
			else
				threadPool.schedule(this, getDelay(queue.peek().getAlertTime()), TimeUnit.SECONDS);
		}

	}
}
