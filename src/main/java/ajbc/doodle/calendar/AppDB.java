package ajbc.doodle.calendar;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.Unit;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;

@Component
public class AppDB {

	@Autowired
	UserService userService;

	@Autowired
	EventService eventService;

	@Autowired
	NotificationService notificationService;

	@EventListener
	public void seedDb(ContextRefreshedEvent contextRefreshedEvent) {
//		seedUsersTable();
//		seedEventsTable();
//		seedNotificationTable();

//		try {
//			Notification notification = notificationService.getNotificationById(1303);
//			notification.setUnit(Unit.HOURS);
//			notification = notificationService.updateNotification(notification);		
//			
//		} catch (DaoException e) {
//			e.printStackTrace();
//		}

	}

	private void seedNotificationTable() {
//		try {
//
//		} catch (DaoException e) {
//			e.printStackTrace();
//		}		
	}

	private void seedEventsTable() {
		try {
			User user1 = userService.getUserById(1015);
			User user2 = userService.getUserById(1016);

			eventService.addEventByUser(1015, new Event(1015, "Wedding", "Emek Hefer", false, LocalDateTime.now(),
					LocalDateTime.now().plusHours(1), "Sen", Arrays.asList(user1, user2)));

			User user3 = userService.getUserById(1000);
			User user4 = userService.getUserById(1013);
			User user5 = userService.getUserById(1014);

			eventService.addEventByUser(1015, new Event(1015, "Vacation", "Eilat", false, LocalDateTime.now(),
					LocalDateTime.now().plusDays(4), "Queen Sheva", Arrays.asList(user3, user4, user5)));

			eventService.addEventByUser(1000, new Event(1000, "Exam", "Experis", false, LocalDateTime.now(),
					LocalDateTime.now().plusHours(2), "Java", Arrays.asList(user3)));

		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	private void seedUsersTable() {
		try {

			userService.addUser(new User("Sapir", "Shlomov", "sapir@test.com"));

		} catch (DaoException e) {
			e.printStackTrace();
		} catch (DataIntegrityViolationException e) {
			System.out.println("The email is already in use");
		}
	}
}
