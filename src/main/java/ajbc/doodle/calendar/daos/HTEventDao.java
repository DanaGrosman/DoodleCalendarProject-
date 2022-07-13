package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;

@SuppressWarnings("unchecked")
@Repository("htEventDao")
public class HTEventDao implements EventDao {

	@Autowired
	private HibernateTemplate template;

	@Override
	public void addEventToDB(Event event) throws DaoException {
		// open session /connection to db
		template.persist(event);
		// close session
	}
	
	@Override
	public List<Event> getAllEvents() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		return (List<Event>) template.findByCriteria(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}

	@Override
	public Event getEventById(Integer id) throws DaoException {
		Event event = template.get(Event.class, id);
		if (event == null)
			throw new DaoException("No such event in DB");
		return event;
	}
	
	@Override
	public List<Event> getEventsByUserId(Integer userId) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		criteria.add(Restrictions.eq("ownerId", userId));
		List<Event> events = ((List<Event>) template.findByCriteria(criteria));
		return events;
	}
	
	@Override
	public List<Event> getEventsByRange(LocalDateTime start, LocalDateTime end) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		criteria.add(Restrictions.ge("startDateTime", start));
		criteria.add(Restrictions.le("endDateTime", end));
		List<Event> events = ((List<Event>) template.findByCriteria(criteria));
		return events;
	}
	
	@Override
	public void updateEvent(Event event) throws DaoException {
		template.merge(event);
	}

	@Override
	public void deleteEvent(Event event) throws DaoException {
		template.delete(event);
	}
}