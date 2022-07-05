package ajbc.doodle.calendar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.entities.Event;

@Service
public class EventService {

	@Autowired
	@Qualifier("htEventDao")
	EventDao eventDao;

	public void addEventByUser(Integer userId, Event event) throws DaoException {
		eventDao.addEventToDB(userId, event);
	}

	public Event getEventById(Integer id) throws DaoException {
		Event event = eventDao.getEventById(id);
		return event;
	}
}
