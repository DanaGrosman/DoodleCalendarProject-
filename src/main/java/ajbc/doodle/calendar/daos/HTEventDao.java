package ajbc.doodle.calendar.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;

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
	public Event getEventById(Integer id) throws DaoException {
		Event event = template.get(Event.class, id);
		if (event == null)
			throw new DaoException("No such event in DB");
		return event;
	}
	
	@Override
	public void updateEvent(Event event) throws DaoException {
		template.merge(event);
	}
}
