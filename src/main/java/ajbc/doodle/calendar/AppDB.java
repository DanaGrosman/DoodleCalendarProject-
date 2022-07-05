package ajbc.doodle.calendar;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;

@Component
public class AppDB {

	@Autowired
	UserService userService;

	@Autowired
	EventService eventService;

	@EventListener
	public void seedDb(ContextRefreshedEvent event) {
//		seedUsersTable();
//		seedEventsTable();

	}

	private void seedEventsTable() {
		try {
			eventService.addEventByUser(1014, new Event(1014, "Wedding", "Adaia", false, LocalDateTime.now(),
					LocalDateTime.now().plusHours(3), "Sivan&Menashe wedding"));
			eventService.addEventByUser(1016, new Event(1014, "Exam", "Afeka College", false, LocalDateTime.now(),
					LocalDateTime.now().plusHours(2), "Math"));
			eventService.addEventByUser(1015, new Event(1015, "Exam", "Experis", false, LocalDateTime.now(),
					LocalDateTime.now().plusHours(2), "JAVA"));
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (DataIntegrityViolationException e) {
			System.out.println("The email is already in use");
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
