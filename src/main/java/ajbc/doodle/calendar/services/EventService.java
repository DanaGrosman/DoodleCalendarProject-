package ajbc.doodle.calendar.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;

@Service
public class EventService {

	@Autowired
	@Qualifier("htEventDao")
	EventDao eventDao;

	@Autowired
	@Qualifier("htNotificationDao")
	NotificationDao notificationDao;

	@Autowired
	@Qualifier("htUserDao")
	UserDao userDao;

	public void addEventByUser(Integer userId, Event event) throws DaoException {
		event.setOwnerId(userId);
		eventDao.addEventToDB(event);
		if (event.getGuests() != null)
			for (int i = 0; i < event.getGuests().size(); i++) {
				Notification notification = new Notification(event.getEventId(), event.getGuests().get(i).getUserId(),
						event.getStartDateTime());
				notification.setEvent(eventDao.getEventById(event.getEventId()));
				notification.setUser(userDao.getUserById(event.getGuests().get(i).getUserId()));
				notificationDao.addNotificationToDB(notification);
			}
	}

	public List<Event> getAllEvents() throws DaoException {
		return eventDao.getAllEvents();
	}

	public Event getEventById(Integer id) throws DaoException {
		Event event = eventDao.getEventById(id);
		return event;
	}

	public List<Event> getEventsByUserId(Integer userId) throws DaoException {
		List<Event> events = new ArrayList<Event>();
		events.addAll(userDao.getUserById(userId).getUserEvents());
		return events;
	}

	public List<Event> getUpcomingEventsByUserId(Integer userId) throws DaoException {
		List<Event> events = getEventsByUserId(userId);
		events = events.stream().filter(e -> e.getStartDateTime().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList());
		for (int i = 0; i < events.size(); i++) {
			events.get(i).setGuests(null);
		}
		return events;
	}

	public List<Event> getEventsByRange(LocalDateTime start, LocalDateTime end) throws DaoException {
		List<Event> events = eventDao.getEventsByRange(start, end);
		return events;
	}

	public List<Event> getEventsByRangeAndUserId(Integer userId, LocalDateTime start, LocalDateTime end)
			throws DaoException {
		List<Event> events = getEventsByUserId(userId);
		events = events.stream()
				.filter(e -> (e.getStartDateTime().isAfter(start) && e.getStartDateTime().isBefore(end)))
				.collect(Collectors.toList());
		return events;
	}

	public Event updateEvent(Integer userId, Event event) throws DaoException {
		if (userId.equals(event.getOwnerId())) {
			eventDao.updateEvent(event);
			event = eventDao.getEventById(event.getEventId());
		} else
			System.out.println("You are not the owner of this event");
		return event;
	}

	public Event softDeleteEvent(Integer eventId) throws DaoException {
		Event event = eventDao.getEventById(eventId);
		event.setActive(false);
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.addAll(event.getNotifications());
		
		for (int i = 0; i < notifications.size(); i++) {
			notifications.get(i).setActive(false);
			notificationDao.updateNotification(notifications.get(i));
		}
		
		eventDao.updateEvent(event);
		return event;
	}

	public Event hardDeleteEvent(Integer eventId) throws DaoException {
		Event event = eventDao.getEventById(eventId);
		eventDao.deleteEvent(event);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.addAll(event.getNotifications());
		
		for (int i = 0; i < notifications.size(); i++) {
			notificationDao.deleteNotification(notifications.get(i));
		}
		
		return event;
	}

}
