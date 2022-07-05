package ajbc.doodle.calendar.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.User;

@SuppressWarnings("unchecked")
@Repository("htUserDao")
public class HTUserDao implements UserDao {

	@Autowired
	private HibernateTemplate template;

	@Override
	public void addUserToDB(User user) throws DaoException {
		// open session /connection to db
		template.persist(user);
		// close session
	}

	@Override
	public User getUserById(Integer id) throws DaoException {
		User user = template.get(User.class, id);
		if (user == null)
			throw new DaoException("No such user in DB");
		return user;
	}
}
