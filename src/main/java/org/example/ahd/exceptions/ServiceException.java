package org.example.ahd.exceptions;

/**
 * Custom exception for Service functionalities related errors.
 * <p>
 *     This is an unchecked Exception.
 * </p>
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
