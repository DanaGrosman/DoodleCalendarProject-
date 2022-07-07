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
//			Event event = eventService.getEventById(1033);
//			event.setTitle("Mall2");
//			event = eventService.updateEvent(1000, event);		
//			
//		} catch (DaoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	private void seedNotificationTable() {
//		try {
//
//		} catch (DaoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
	}

	private void seedEventsTable() {
		try {
//			eventService.addEventByUser(1014, new Event(1014, "Wedding", "Adaia", false, LocalDateTime.now(),
//					LocalDateTime.now().plusHours(3), "Sivan&Menashe wedding"));
//			eventService.addEventByUser(1016, new Event(1014, "Exam", "Afeka College", false, LocalDateTime.now(),
//					LocalDateTime.now().plusHours(2), "Math"));
//			eventService.addEventByUser(1015, new Event(1015, "Exam", "Experis", false, LocalDateTime.now(),
//					LocalDateTime.now().plusHours(2), "JAVA"));

			User user1 = userService.getUserById(1015);
			User user2 = userService.getUserById(1016);
			
			eventService.addEventByUser(1015, new Event(1015, "Wedding", "Emek Hefer", false, LocalDateTime.now(),
					LocalDateTime.now().plusHours(1), "Sen", Arrays.asList(user1, user2)));
			
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
