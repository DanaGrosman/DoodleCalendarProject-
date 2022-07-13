package ajbc.doodle.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;

class TestUser {
	User userTest = new User();
	
	@Test
	void checkConstructor() {
		String firstName = "Shilat", lastName = "Grosman", email = "shilt@test.com";
		User user = new User(firstName, lastName, email);
		assertNotNull(user);
		assertNull(user.getUserId());
		assertNull(user.getUserEvents());
		assertEquals(firstName, user.getFirstName());
		assertEquals(lastName, user.getLastName());
		assertEquals(email, user.getEmail());
		assertEquals(true, user.getIsActive());
		assertEquals(false, user.getIsLogged());
		assertNull(user.getSubscriptionData());
	}

	@Test
	void checkSetFirstName() {
		userTest.setFirstName("Ori");
		assertEquals("Ori", userTest.getFirstName());
	}

	@Test
	void checkSetLastName() {
		userTest.setLastName("Kaliski");
		assertEquals("Kaliski", userTest.getLastName());
	}

	@Test
	void checkSetEmail() {
		userTest.setEmail("ori@test.com");
		assertEquals("ori@test.com", userTest.getEmail());
	}

	@Test
	void checkSetIsActive() {
		userTest.setIsActive(true);
		assertEquals(true, userTest.getIsActive());
	}

	@Test
	void checkSetIsLogged() {
		userTest.setIsLogged(true);
		assertEquals(true, userTest.getIsLogged());
	}

	@Test
	void checkSetUserEvents() {
		Event event = new Event();
		event.setTitle("Dinner");
		event.setStartDateTime(LocalDateTime.now());
		event.setEndDateTime(LocalDateTime.now().plusHours(3));
		event.setDescription("Humburger");
		event.setAddress("TLV");

		Set<Event> events = Set.of(event);
		userTest.setUserEvents(events);

		Set<Event> userEvents = userTest.getUserEvents();

		assertFalse(userEvents.isEmpty());
		assertEquals(events, userEvents);
		assertTrue(events.size() == userEvents.size());
	}
}
