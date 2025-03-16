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
    public void addAll(List<User> users) {
       for(User user : users) {
          this.add(user);
       }
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

    @Override
    public void cleanSchema() {
        cleanTables();
        dropTables();
    }

    private void cleanTables() {
        nativeQuery("ALTER TABLE users DROP FOREIGN KEY FK98xr91mc3rou6mjopt3pgq9vj");
        nativeQuery("TRUNCATE TABLE cars");
        nativeQuery("TRUNCATE TABLE users");
    }

    private void dropTables() {
        nativeQuery("DROP TABLE IF EXISTS cars");
        nativeQuery("DROP TABLE IF EXISTS users");
    }

    private void nativeQuery(String sql) {
        sessionFactory
                .getCurrentSession()
                .createNativeQuery(sql)
                .executeUpdate();
    }
}
