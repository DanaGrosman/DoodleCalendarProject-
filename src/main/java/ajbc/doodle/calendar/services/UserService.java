package ajbc.doodle.calendar.services;

import java.util.ArrayList;
import java.util.List;

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
public class UserService {

	@Autowired
	@Qualifier("htUserDao")
	UserDao userDao;

	@Autowired
	@Qualifier("htEventDao")
	EventDao eventDao;

	@Autowired
	@Qualifier("htNotificationDao")
	NotificationDao notificationDao;

	public void addUser(User user) throws DaoException {
		userDao.addUserToDB(user);
	}

	public void addListOfUsers(List<User> users) throws DaoException {
		userDao.addListOfUsersToDB(users);
	}

	public List<User> getAllUsers() throws DaoException {
		return userDao.getAllUsers();
	}

	public User getUserById(Integer id) throws DaoException {
		User user = userDao.getUserById(id);
		return user;
	}

	public User getUserByEmail(String email) throws DaoException {
		User user = userDao.getUserByEmail(email);
		return user;
	}

	public List<User> getUsersByEventId(Integer eventId) throws DaoException {
		return userDao.getUsersByEventId(eventId);
	}

	public User updateUser(Integer id, User user) throws DaoException {
		userDao.updateUser(user);
		user = userDao.getUserById(id);
		return user;
	}

	public User softDeleteUser(Integer userId) throws DaoException {
		User user = userDao.getUserById(userId);
		user.setIsActive(false);

		List<Event> events = new ArrayList<Event>();
		events.addAll(user.getUserEvents());
		events = events.stream().filter(e -> e.getOwnerId().equals(userId)).toList();

		for (int i = 0; i < events.size(); i++) {
			events.get(i).setActive(false);
			eventDao.updateEvent(events.get(i));
		}

		List<Notification> notifications = notificationDao.getNotificationByUserId(userId);

		for (int i = 0; i < notifications.size(); i++) {
			notifications.get(i).setActive(false);
			notificationDao.updateNotification(notifications.get(i));
		}

		userDao.updateUser(user);
		return user;
	}

	public User hardDeleteUser(Integer userId) throws DaoException {
		User user = userDao.getUserById(userId);

		List<Event> events = new ArrayList<Event>();
		events.addAll(user.getUserEvents());
		events = events.stream().filter(e -> e.getOwnerId().equals(userId)).toList();

		if (!events.isEmpty())
			for (int i = 0; i < events.size(); i++) {
				List<Notification> notifications = notificationDao.getNotificationByEventId(events.get(i).getEventId());
				notifications.addAll(notificationDao.getNotificationByUserId(user.getUserId()));

				if (!notifications.isEmpty())
					for (int j = 0; j < notifications.size(); j++) {
						notificationDao.deleteNotification(notifications.get(j));
					}

				eventDao.deleteEvent(events.get(i));
			}

		userDao.deleteUser(user);
		return user;
	}
}
