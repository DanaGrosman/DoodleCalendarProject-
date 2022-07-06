package ajbc.doodle.calendar.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Notification;

@SuppressWarnings("unchecked")
@Repository("htNotificationDao")
public class HTNotificationDao implements NotificationDao {

	@Autowired
	private HibernateTemplate template;

	@Override
	public void addNotificationToDB(Notification notification) throws DaoException {
		// open session /connection to db
		template.persist(notification);
		// close session
	}

	@Override
	public Notification getNotificationById(Integer id) throws DaoException {
		Notification notification = template.get(Notification.class, id);
		if (notification == null)
			throw new DaoException("No such notification in DB");
		return notification;
	}
	
	@Override
	public void updateNotification(Notification notification) throws DaoException {
		template.merge(notification);
	}
}
