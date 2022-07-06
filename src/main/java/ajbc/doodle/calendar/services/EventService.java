package ajbc.doodle.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.Unit;

@Service
public class EventService {

	@Autowired
	@Qualifier("htEventDao")
	EventDao eventDao;

	@Autowired
	@Qualifier("htNotificationDao")
	NotificationDao notificationDao;

	public void addEventByUser(Integer userId, Event event) throws DaoException {
		eventDao.addEventToDB(event);
		for (int i = 0; i < event.getGuests().size(); i++)
			notificationDao.addNotificationToDB(new Notification(event.getGuests().get(i).getUserId(),
					event.getEventId(), Unit.MINUTES, 0, event.getStartDateTime()));
	}

	public Event getEventById(Integer id) throws DaoException {
		Event event = eventDao.getEventById(id);
		return event;
	}

	public Event updateEvent(Integer userId, Event event) throws DaoException {
		if (userId.equals(event.getOwnerId())) {
			eventDao.updateEvent(event);
			event = eventDao.getEventById(event.getEventId());
		} else
			System.out.println("You are not the owner of this event");
		return event;
	}

	public List<Event> getAllEvents() throws DaoException {
		return eventDao.getAllEvents();
	}
}
