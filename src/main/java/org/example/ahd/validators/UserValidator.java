package org.example.ahd.validators;

import org.example.ahd.domain.User;
import org.example.ahd.domain.UserType;
import org.example.ahd.exceptions.ValidationException;

/**
 * Validator class for the {@link User} entity.
 * Implements the {@link IValidator} interface.
 */
public class UserValidator implements IValidator<User>{
    /**
     * Validates an {@link User} entity.
     * @param entity the user to be validated
     * @throws org.example.ahd.exceptions.ValidationException if the user does not comply with the business logic.
     */
    @Override
    public void validate(User entity) {
        StringBuilder errors = new StringBuilder();

        if (entity.getEmail() == null) {
            errors.append("Email is required!\n");
        }
        else if(!entity.getEmail().matches("\"\\w+@\\w+\\.\\w+\"gm")) {
            errors.append("Email address is invalid!\n");
        }

        if (entity.getPassword().isEmpty()) {
            errors.append("Password is required!\n");
        }
        else if (entity.getPassword().length() < 8) {
            errors.append("Password length must be at least 8 characters!\n");
        }

        if (entity.getUsername().isEmpty()) {
            errors.append("Username is required!\n");
        }
        else if (entity.getUsername().length() < 4) {
            errors.append("Username length must be at least 4 characters!\n");
        }

        if (entity.getType() == null){
            errors.append("UserType is required!\n");
        }
        else if (!entity.getType().equals(UserType.ATC) && entity.getType().equals(UserType.CLEANUP)) {
            errors.append("UserType must be 'ATC' or 'CLEANUP'!\n");
        }

        if (!errors.isEmpty()){
            throw new ValidationException(errors.toString());
        }
    }
}
