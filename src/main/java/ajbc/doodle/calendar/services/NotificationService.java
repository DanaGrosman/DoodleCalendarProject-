package ajbc.doodle.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Notification;

@Service
public class NotificationService {

	@Autowired
	@Qualifier("htNotificationDao")
	NotificationDao notificationDao;

	@Autowired
	@Qualifier("htEventDao")
	EventDao eventDao;

	@Autowired
	@Qualifier("htUserDao")
	UserDao userDao;

	public void addNotification(Notification notification) throws DaoException {
		notification.setEvent(eventDao.getEventById(notification.getEventId()));
		notification.setUser(userDao.getUserById(notification.getUserId()));
		notificationDao.addNotificationToDB(notification);
	}

	public List<Notification> getAllNotifications() throws DaoException {
		return notificationDao.getAllNotifications();
	}

	public Notification getNotificationById(Integer id) throws DaoException {
		Notification notification = notificationDao.getNotificationById(id);
		return notification;
	}

	public List<Notification> getNotificationByUserId(Integer userId) throws DaoException {
		List<Notification> notifications = notificationDao.getNotificationByUserId(userId);
		return notifications;
	}

	public List<Notification> getNotificationByEventId(Integer eventId) throws DaoException {
		List<Notification> notifications = notificationDao.getNotificationByEventId(eventId);
		return notifications;
	}
	
	public List<Notification> getNotificationsNotAlerted() throws DaoException {
		List<Notification> notifications = notificationDao.getNotificationsNotAlerted();
		return notifications;
	}

	public Notification updateNotification(Notification notification) throws DaoException {
		notification.setEvent(eventDao.getEventById(notification.getEventId()));
		notification.setUser(userDao.getUserById(notification.getUserId()));
		notificationDao.updateNotification(notification);
		notification = notificationDao.getNotificationById(notification.getNotificationId());
		return notification;
	}

	public Notification softDeleteNotification(Integer notificationId) throws DaoException {
		Notification notification = notificationDao.getNotificationById(notificationId);
		notification.setActive(false);
		notificationDao.updateNotification(notification);
		return notification;
	}
	
	public Notification hardDeleteNotification(Integer notificationId) throws DaoException {
		Notification notification = notificationDao.getNotificationById(notificationId);
		notificationDao.deleteNotification(notification);
		return notification;
	}
}
