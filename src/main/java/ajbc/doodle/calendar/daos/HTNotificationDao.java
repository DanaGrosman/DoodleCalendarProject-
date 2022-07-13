package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
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
	public List<Notification> getAllNotifications() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		return (List<Notification>) template
				.findByCriteria(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}

	@Override
	public Notification getNotificationById(Integer id) throws DaoException {
		Notification notification = template.get(Notification.class, id);
		if (notification == null)
			throw new DaoException("No such notification in DB");
		return notification;
	}

	@Override
	public List<Notification> getNotificationByUserId(Integer userId) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.add(Restrictions.eq("userId", userId));
		List<Notification> notifications = ((List<Notification>) template.findByCriteria(criteria));
		return notifications;
	}

	@Override
	public List<Notification> getNotificationByEventId(Integer eventId) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.add(Restrictions.eq("eventId", eventId));
		List<Notification> notifications = ((List<Notification>) template.findByCriteria(criteria));
		return notifications;
	}

	@Override
	public List<Notification> getNotificationsNotAlerted() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("isAlerted", false));
		List<Notification> notifications = ((List<Notification>) template.findByCriteria(criteria));
		notifications.forEach(System.out::println);

		return notifications;
	}

	@Override
	public void updateNotification(Notification notification) throws DaoException {
		template.merge(notification);
	}
	
	@Override
	public void deleteNotification(Notification notification) throws DaoException {
		template.delete(notification);
	}
}
