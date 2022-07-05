package ajbc.doodle.calendar.daos;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Event;


@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface EventDao {

	// CRUD operations
	@Transactional(readOnly = false)
	public default void addEventToDB(Integer userId, Event event) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default Event getEventById(Integer id) throws DaoException {
		throw new DaoException("Method not implemented");
	}
}
