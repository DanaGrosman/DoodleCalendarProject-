package ajbc.doodle.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.User;

@Service
public class UserService {

	@Autowired
	@Qualifier("htUserDao")
	UserDao userDao;

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


}
