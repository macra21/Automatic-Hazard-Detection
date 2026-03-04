package org.example.ahd.service;

import org.example.ahd.domain.User;
import org.example.ahd.repository.UserRepository;
import org.example.ahd.validators.IValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//TODO Check if username and email are unique by threating DataBase Exceptions on the business layer

/**
 * Service class for {@link User} entity.
 * <p>
 * Includes CRUD operations with the Database and
 * more complex functionalities.
 * </p>
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final IValidator<User> userValidator;

    /**
     * Constructs a fully initialized User Service.
     *
     * @param userRepository the repository used by this service
     * @param userValidator the validator used by this service
     */
    UserService(UserRepository userRepository, IValidator<User> userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    /**
     * Saves a {@link User} to the database.
     *
     * @param user the user to be added to the database
     * @throws org.example.ahd.exceptions.ValidationException if the User is invalid
     * @throws org.example.ahd.exceptions.DatabaseException if the database throws an error(e.g. the user is already in the database, etc.)
     */
    @Transactional
    public void addUser(User user) {
        userValidator.validate(user);
        userRepository.add(user);
    }

    /**
     * Updates a {@link User} in the database.
     *
     * @param user the user to be updated in the database
     * @throws org.example.ahd.exceptions.ValidationException if the User is invalid
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails(e.g. the user does not have a unique username/email, etc.)
     */
    @Transactional
    public void updateUser(User user) {
        userValidator.validate(user);
        userRepository.update(user);
    }

    /**
     * Deletes a {@link User} in the database based on its ID.
     *
     * @param ID the id of the user to be deleted in the database
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails(e.g. the user was not found, etc.)
     */
    @Transactional
    public void deleteUser(Integer ID) {
        userRepository.deleteById(ID);
    }

    /**
     * Returns a {@link User} by its ID.
     *
     * @param ID the ID of the searched user
     * @return the user if found or null if not found
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    @Transactional
    public User findUserById(Integer ID) {
        return userRepository.findById(ID);
    }

    /**
     * Returns all the users from the database.
     * <p>
     * <strong>Warning: </strong> Use this function carefully because the database can be quite large.
     * </p>
     *
     * @return a list of all the users
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    /**
     * Returns a {@link User} based on its email and password
     * @param email the email of the searched user
     * @param password the password of the searched user
     * @return the user if found or null if not found
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    @Transactional
    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findByMailAndPassword(email, password);
    }

    /**
     * Returns a {@link User} based on its email
     * @param email the email of the searched user
     * @return the user if found or null if not found
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Returns a {@link User} based on its username
     * @param username the username of the searched user
     * @return the user if found or null if not found
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
