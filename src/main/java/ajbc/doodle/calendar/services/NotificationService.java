package ajbc.doodle.calendar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.entities.Notification;

@Service
public class NotificationService {

	@Autowired
	@Qualifier("htNotificationDao")
	NotificationDao notificationDao;

	public void addNotificationByEventAndUser(Notification notification) throws DaoException {
		notificationDao.addNotificationToDB(notification);
	}

	public Notification getNotificationById(Integer id) throws DaoException {
		Notification notification = notificationDao.getNotificationById(id);
		return notification;
	}
	
	public Notification updateNotification(Integer id, Notification notification) throws DaoException {
		notificationDao.updateNotification(notification);
		notification = notificationDao.getNotificationById(id);
		return notification;
	}
}
