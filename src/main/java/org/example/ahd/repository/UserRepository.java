package org.example.ahd.repository;

import org.example.ahd.domain.User;
import org.example.ahd.exceptions.DatabaseException;
import org.example.ahd.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 * org.example.ahd.AbstractRepository implementation for the {@link User} entity.
 * <p>
 *     Extends the {@link AbstractRepository} to provide standard CRUD operations
 *     and adds specific methods for querying users by email or username.
 * </p>
 */
public class UserRepository extends AbstractRepository<User, Integer> {

    /**
     * Constructs a new UserRepository.
     * Initializes the repository with the {@link User} class type.
     */
    public UserRepository() {
        super(User.class);
    }

    /**
     * Finds a user by their email address.
     * <p>
     *     This method executes an HQL query to find a user with the specified email.
     * </p>
     * @param email the email address to search for
     * @return the {@link User} with the specified email, or null if not found
     * @throws DatabaseException if the operation fails
     */
    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Finds a user by their username.
     * <p>
     *     This method executes an HQL query to find a user with the specified username.
     * </p>
     * @param username the username to search for
     * @return the {@link User} with the specified username, or null if not found
     * @throws DatabaseException if the operation fails
     */
    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Finds a user by their email and password.
     * <p>
     *     This method executes an HQL query to find a user with the specified email and password.
     * </p>
     * @param email the email to search for
     * @param password the hashed password to search for
     * @return the {@link User} with the specified username, or null if not found
     * @throws DatabaseException if the operation fails
     */
    public User findByMailAndPassword(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email and password = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
