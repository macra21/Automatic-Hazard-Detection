package org.example.ahd.repository;

import org.example.ahd.domain.User;
import org.example.ahd.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for the {@link User} entity.
 * <p>
 *     Extends the {@link AbstractRepository} to provide standard CRUD operations
 *     and adds specific methods for querying users by email or username using JPA.
 * </p>
 */
@Repository
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
     * @param email the email address to search for
     * @return the {@link User} with the specified email, or null if not found
     * @throws DatabaseException if the operation fails
     */
    public User findByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getResultList()
                    .stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Finds a user by their username.
     * @param username the username to search for
     * @return the {@link User} with the specified username, or null if not found
     * @throws DatabaseException if the operation fails
     */
    public User findByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultList()
                    .stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Finds a user by their email and password.
     * @param email the email to search for
     * @param password the hashed password to search for
     * @return the {@link User} with the specified username, or null if not found
     * @throws DatabaseException if the operation fails
     */
    public User findByMailAndPassword(String email, String password) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getResultList()
                    .stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
