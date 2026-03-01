package Domain;

/**
 * Represents the User entity within the application domain.
 * Includes basic information such as a
 */
public class User {
    private Integer ID;
    private String email;
    private String password;
    private String username;
    private UserType type;

    public User(Integer ID, String email, String password, String username) {
        this.ID = ID;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
