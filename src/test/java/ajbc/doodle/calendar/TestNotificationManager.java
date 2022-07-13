package ajbc.doodle.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;

class TestNotificationManager {

	NotificationManager manager;

	public TestNotificationManager() throws DaoException {
		this.manager = new NotificationManager();
	}

	@Test
	void testConstructor() {
		assertNotNull(manager.getQueue());
	}

	@Test
	void testGetDelay() {
		LocalDateTime time = LocalDateTime.of(2022, 7, 31, 20, 30);
		LocalDateTime now = LocalDateTime.now();
		long seconds = ChronoUnit.SECONDS.between(now, time);
		assertEquals(seconds, manager.getDelay(time));
	}

	@Test
	public void testPriorityQueue() {
		LocalDateTime timing = LocalDateTime.of(2023, 2, 2, 15, 30);
		Notification notification = new Notification(1226, 1000, timing);
		manager.getQueue().add(notification);

		assertTrue(manager.getQueue().size() == 1);

		Notification notification2 = new Notification(1226, 1000, timing.minusDays(5));
		manager.getQueue().add(notification2);

		assertTrue(manager.getQueue().size() == 2);
		assertEquals(notification2, manager.getQueue().peek());

		manager.getQueue().remove(notification2);

		assertTrue(manager.getQueue().size() == 1);
		assertEquals(notification, manager.getQueue().peek());
	}
}
