package ajbc.doodle.calendar.daos;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Notification;

@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface NotificationDao {

	// CRUD operations
	@Transactional(readOnly = false)
	public default void addNotificationToDB(Notification notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default Notification getNotificationById(Integer id) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void updateNotification(Notification notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}
}