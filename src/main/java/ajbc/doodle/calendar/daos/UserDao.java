package ajbc.doodle.calendar.daos;

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
	public default User getUserById(Integer id) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void updateUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}
}
