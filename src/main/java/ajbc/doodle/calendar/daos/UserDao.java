package ajbc.doodle.calendar.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.User;

@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface UserDao {

	// CRUD operations
	@Transactional(readOnly = false)
	public default void addUserToDB(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void addListOfUsersToDB(List<User> users) throws DaoException{
		throw new DaoException("Method not implemented");
	}
	
	public default List<User> getAllUsers() throws DaoException{
		throw new DaoException("Method not implemented");
	}
	
	public default User getUserById(Integer id) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default User getUserByEmail(String email) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	
	public default List<User> getUsersByEventId(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void updateUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void deleteUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}

}
