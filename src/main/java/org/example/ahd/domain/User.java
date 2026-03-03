package org.example.ahd.domain;

import jakarta.persistence.*;
/**
 * Represents the User entity within the application domain.
 * Includes basic information such as a unique identifier,
 * email, password, username and {@link UserType}.
 */
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private UserType type;

    /**
     * Constructor without arguments required by Hibernate.
     */
    public User(){}

    /**
     * Constructs a fully initialized User object.
     * <p>
     *     This constructor is typically used when the user is retrieved from
     *     the database and already has an assigned identifier.
     * </p>
     * <p>
     *     <b>Note:</b> This constructor was implemented for JDBC compatibility and is preserved for that purpose.
     * </p>
     * @param ID the unique identifier of the user
     * @param email the email of the user
     * @param password the password of the user
     * @param username the username of the user
     * @param type the {@link UserType} of the user
     */
    public User(Integer ID, String email, String password, String username, UserType type) {
        this.ID = ID;
        this.email = email;
        this.password = password;
        this.username = username;
        this.type = type;
    }

    /**
     * Constructs a User object without an identifier.
     * <p>
     *     This constructor is typically used before the user is added to
     *     the database and has no ID. The database will automatically assign
     *     an ID to the hazard if there are no errors.
     * </p>
     * <p>
     *     <b>Note:</b> This constructor was implemented for JDBC compatibility and is preserved for that purpose.
     * </p>
     * @param email the email of the user
     * @param password the password of the user
     * @param username the username of the user
     * @param type the {@link UserType} of the user
     */
    public User(String email, String password, String username, UserType type) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.type = type;
    }

    /**
     * Returns the ID of the user.
     * @return the user's ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * Sets or updates the ID of the user.
     * @param ID the new ID of the user
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * Returns the email of the user.
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets or updates the email of the user.
     * @param email the new email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user.
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets or updates the password of the user.
     * @param password the new password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the username of the user.
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets or updates the username of the user.
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the {@link UserType} of the user.
     * @return the user's type
     */
    public UserType getType() {
        return type;
    }

    /**
     * Sets or updates the {@link UserType} of the user.
     * @param type the new type of the user
     */
    public void setType(UserType type) {
        this.type = type;
    }
}
