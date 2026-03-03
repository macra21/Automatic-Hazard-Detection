package org.example.ahd.repository;

import java.util.List;

/**
 * Generic interface for CRUD (Create, Read, Update, Delete) operations.
 * <p>
 *     This interface defines the standard contract for all repositories in the application.
 *     It uses generics to be type-safe and reusable for any entity type.
 * </p>
 * @param <T> the type of the entity
 * @param <ID> the type of the entity's identifier
 */
public interface IRepository<T, ID> {
    /**
     * Adds a new entity to the repository.
     * @param entity the entity to be added
     */
    void add(T entity);

    /**
     * Updates an existing entity in the repository.
     * @param entity the entity with updated information
     */
    void update(T entity);

    /**
     * Removes an entity from the repository by its unique identifier.
     * @param id the ID of the entity to be removed
     */
    void deleteById(ID id);

    /**
     * Retrieves an entity by its unique identifier.
     * @param id the unique identifier of the entity
     * @return the entity if found, or null if not found
     */
    T findById(ID id);

    /**
     * Retrieves all entities of type T from the repository.
     * @return a list containing all entities found
     */
    List<T> getAll();
}
