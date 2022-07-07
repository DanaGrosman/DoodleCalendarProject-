package ajbc.doodle.calendar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;

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
	
	public void addNotificationByEventAndUser(Notification notification) throws DaoException {
//		Event event = eventDao.getEventById(notification.getEventId());
//		User user = userDao.getUserById(notification.getUserId());
//		notification.setEvent(event);
//		notification.setUser(user);
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
