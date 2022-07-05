package ajbc.doodle.calendar.services;

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

	public User getUserById(Integer id) throws DaoException {
		User user = userDao.getUserById(id);
		return user;
	}
}
