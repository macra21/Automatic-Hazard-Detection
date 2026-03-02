package Exceptions;

/**
 * Custom exception for database-related errors.
 * <p>
 *     This exception wraps any exception thrown by the DB or Hibernate
 *     for a better management of errors in the application.
 * </p>
 * This is an unchecked Exception.
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
