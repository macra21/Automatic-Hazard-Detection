package org.example.ahd.dto;

/**
 * Data Transfer Object (DTO) for user login requests.
 * <p>
 *     This class encapsulates the data required for a user to authenticate,
 *     specifically the email and password. It is used to transfer data from
 *     the client (frontend) to the {@link org.example.ahd.controller.AuthentificationController}.
 * </p>
 */
public class LoginRequest {
    private String email;
    private String password;

    /**
     * Returns the email address of the user attempting to log in.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets or updates the email address for the login request.
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user attempting to log in.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets or updates the password for the login request.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
