package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory
                .getCurrentSession()
                .createQuery(
                        "SELECT user FROM User user JOIN FETCH user.car"
                );

        return query.getResultList();
    }

    @Override
    public User getUserByCar(String model, int series) {
        String hql = "SELECT user " +
                "FROM User user " +
                "JOIN FETCH user.car car " +
                "WHERE car.model = :model AND car.series = :series";

        TypedQuery<User> query = sessionFactory
                .getCurrentSession()
                .createQuery(hql, User.class);

        query.setParameter("model", model);
        query.setParameter("series", series);

        List<User> users = query.getResultList();

        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }
}
