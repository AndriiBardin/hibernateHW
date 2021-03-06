package cinema.dao.impl;

import cinema.dao.ShoppingCartDao;
import cinema.exception.DataBaseException;
import cinema.model.ShoppingCart;
import cinema.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    private final SessionFactory sessionFactory;

    public ShoppingCartDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(shoppingCart);
            transaction.commit();
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataBaseException("Can't add shopping cart entity " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<ShoppingCart> getByUser = session.createQuery("from ShoppingCart sc "
                    + "left join fetch sc.tickets where sc.user =:user", ShoppingCart.class);
            getByUser.setParameter("user", user);
            return getByUser.getSingleResult();
        } catch (Exception e) {
            throw new DataBaseException("Can't get user " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(shoppingCart);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataBaseException("Can't update shopping cart entity " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
