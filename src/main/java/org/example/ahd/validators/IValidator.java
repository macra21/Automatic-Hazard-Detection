package org.example.ahd.validators;

/**
 * Generic interface for validator classes.
 * <p>
 *     This interface defines the standard contract for all validators in the application.
 *     It uses generics to be type-safe and reusable for any entity type.
 * </p>
 * @param <E> the type of the entity
 */
public interface IValidator <E>{
    /**
     * Validates the entity.
     * @throws org.example.ahd.exceptions.ValidationException if the object does not follow the business rules.
     */
    public void validate(E entity);
}
