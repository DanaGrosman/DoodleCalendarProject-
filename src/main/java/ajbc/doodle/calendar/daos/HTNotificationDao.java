package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
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
	
	@Override
	public List<Notification> getAllNotifications() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		return (List<Notification>) template.findByCriteria(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}
}
