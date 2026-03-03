package org.example.ahd.exceptions;

/**
 * Custom exception for entity validation errors.
 * <p>
 *     This exception is thrown when a domain entity does not
 *     comply with the existing business rules.
 * </p>
 * This is an unchecked Exception.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
