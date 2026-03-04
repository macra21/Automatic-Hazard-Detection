package org.example.ahd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.ahd.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Abstract implementation of the {@link IRepository} interface using JPA EntityManager.
 * <p>
 *     This class provides the core implementation for CRUD operations using the
 *     standard JPA {@link EntityManager}. Transaction management is handled by Spring's @Transactional.
 * </p>
 * @param <T> the type of the entity
 * @param <ID> the type of the entity's identifier
 */
@Repository
public abstract class AbstractRepository<T, ID> implements IRepository<T, ID> {
    
    @PersistenceContext
    protected EntityManager entityManager;
    
    private final Class<T> entityClass;

    /**
     * Constructs a new AbstractRepository for the specified entity class.
     * @param entityClass the class of the entity type T
     */
    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Adds a new entity to the database.
     * @param entity the entity to be added
     * @throws DatabaseException if the operation fails
     */
    @Override
    public void add(T entity) {
        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Updates an existing entity in the database.
     * @param entity the entity with updated information
     * @throws DatabaseException if the operation fails
     */
    @Override
    public void update(T entity) {
        try {
            entityManager.merge(entity);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Removes an entity from the database.
     * @param id the ID of the entity to be removed
     * @throws DatabaseException if the operation fails
     */
    @Override
    public void deleteById(ID id) {
        try {
            T entity = entityManager.find(entityClass, id);
            if (entity != null) {
                entityManager.remove(entity);
            }
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Retrieves an entity by its unique identifier.
     * @param id the unique identifier of the entity
     * @return the entity if found, or null if not found
     * @throws DatabaseException if the operation fails
     */
    @Override
    public T findById(ID id) {
        try {
            return entityManager.find(entityClass, id);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Retrieves all entities of type T from the database.
     * @return a list containing all entities found
     * @throws DatabaseException if the operation fails
     */
    @Override
    public List<T> getAll() {
        try {
            return entityManager.createQuery("from " + entityClass.getName(), entityClass).getResultList();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
