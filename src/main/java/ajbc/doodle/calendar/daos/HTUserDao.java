package ajbc.doodle.calendar.daos;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
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
	public void addListOfUsersToDB(List<User> users) throws DaoException {
		// open session /connection to db
		template.persist(users);
		// close session
	}

	@Override
	public List<User> getAllUsers() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		List<User> users = (List<User>) template
				.findByCriteria(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
//		for (int i = 0; i < users.size(); i++) {
//			for (Event event : users.get(i).getUserEvents()) {
//				filterNotificationsByUser(event, users.get(i).getUserId());
//			}
//		}
		return users;
	}

	private void filterNotificationsByUser(Event event, Integer userId) {
		Set<Notification> nots = event.getNotifications();
		System.out.println("-------" + userId);

		System.out.println("----before not id's");
		nots.forEach(t -> System.out.println(t.getUserId()));
		
		nots = nots.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toSet());
		
		System.out.println("----after not id's");
		nots.forEach(t -> System.out.println(t.getUserId()));
		
		
		event.setNotifications(nots.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toSet()));
	}

	@Override
	public User getUserById(Integer id) throws DaoException {
		User user = template.get(User.class, id);
		if (user == null)
			throw new DaoException("No such user in DB");
		return user;
	}

	@Override
	public User getUserByEmail(String email) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("email", email));
		List<User> users = ((List<User>) template.findByCriteria(criteria));
		return users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public List<User> getUsersByEventId(Integer eventId) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("eventId", eventId));
		return ((List<User>) template.findByCriteria(criteria));
	}

	@Override
	public void updateUser(User user) throws DaoException {
		template.merge(user);
	}

}
