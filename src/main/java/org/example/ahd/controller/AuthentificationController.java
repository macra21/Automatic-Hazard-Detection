package org.example.ahd.controller;

import org.example.ahd.domain.User;
import org.example.ahd.dto.LoginRequest;
import org.example.ahd.exceptions.ValidationException;
import org.example.ahd.service.AuthentificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling user authentication.
 * <p>
 *     This controller exposes endpoints for user login and registration.
 *     It acts as an interface between the frontend (Angular) and the backend services.
 * </p>
 */
@RestController                                 //  This uses REST API (basically it says that its answers are not html pages but JSON objects)
@RequestMapping("/api/auth")                 // Common prefix for the methods (every request to this controller must start with http://localhost:8080/api/auth)
@CrossOrigin(origins = "http://localhost:4200") // Only trusts requests from the http://localhost:4200 origin (my address is http://localhost:8080)
public class AuthentificationController {
    private final AuthentificationService authService;

    /**
     * Constructs the AuthentificationController with the required service.
     * @param authService the service handling authentication logic
     */
    public AuthentificationController(AuthentificationService authService) {
        this.authService = authService;
    }

    /**
     * Handles user login requests.
     * <p>
     *     Receives a {@link LoginRequest} containing email and password,
     *     attempts to authenticate the user, and returns the user details if successful.
     * </p>
     * @param loginRequest the DTO containing login credentials
     * @return a {@link ResponseEntity} containing the {@link User} if successful, or an error message
     */
    @PostMapping("/login")  //POST is used for security reasons(if I would've used GET the url would've contained the email and password! Big no no)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    /**
     * Handles user registration requests.
     * <p>
     *     Receives a {@link User} object, validates it, and registers the new user.
     * </p>
     * @param user the user entity to be registered
     * @return a {@link ResponseEntity} with a success message or error details
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            authService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration: " + e.getMessage());
        }
    }

    /**
     * Handles user deletion requests.
     *     Receives a {@link LoginRequest} containing email and password,
     *     attempts to delete the user, and returns a confirmation message if successful.
     * </p>
     * @param loginRequest the DTO containing login credentials
     * @return a {@link ResponseEntity} containing a confirmation message if successful, or an error message
     */
    @PostMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestBody LoginRequest loginRequest) {
        try {
            authService.deleteAccount(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok("Account deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during account deletion: " + e.getMessage());
        }
    }
}
