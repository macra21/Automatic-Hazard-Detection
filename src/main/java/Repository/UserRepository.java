package Repository;

import Domain.User;
import Exceptions.DatabaseException;
import Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 * Repository implementation for the {@link User} entity.
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
     *     It assumes that email addresses are unique.
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
     *     It assumes that usernames are unique.
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
}
