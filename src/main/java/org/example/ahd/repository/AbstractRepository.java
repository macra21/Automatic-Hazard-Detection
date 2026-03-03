package org.example.ahd.repository;

import org.example.ahd.exceptions.DatabaseException;
import org.example.ahd.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * Abstract implementation of the {@link IRepository} interface using Hibernate.
 * <p>
 *     This class provides the core implementation for CRUD operations using the
 *     Hibernate {@link Session} and {@link Transaction} management.
 *     It handles the boilerplate code for opening sessions, managing transactions,
 *     and closing resources.
 * </p>
 * @param <T> the type of the entity
 * @param <ID> the type of the entity's identifier
 */
public abstract class AbstractRepository<T, ID> implements IRepository<T, ID> {
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
     * <p>
     *     This method opens a new Hibernate session, begins a transaction,
     *     saves the entity, and commits the transaction. If an error occurs,
     *     the transaction is rolled back.
     * </p>
     * @param entity the entity to be added
     * @throws DatabaseException if the operation fails
     */
    @Override
    public void add(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Updates an existing entity in the database.
     * <p>
     *     This method opens a new Hibernate session, begins a transaction,
     *     updates the entity, and commits the transaction. If an error occurs,
     *     the transaction is rolled back.
     * </p>
     * @param entity the entity with updated information
     * @throws DatabaseException if the operation fails
     */
    @Override
    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Removes an entity from the database.
     * <p>
     *     This method opens a new Hibernate session, begins a transaction,
     *     deletes the entity, and commits the transaction. If an error occurs,
     *     the transaction is rolled back.
     * </p>
     * @param entity the entity to be removed
     * @throws DatabaseException if the operation fails
     */
    @Override
    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Retrieves an entity by its unique identifier.
     * <p>
     *     This method opens a new Hibernate session and retrieves the entity
     * </p>
     * @param id the unique identifier of the entity
     * @return the entity if found, or null if not found
     * @throws DatabaseException if the operation fails
     */
    @Override
    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Cool info: Hibernate looks for the object in cache and only if
            // it does not find it, it will execute the query
            return session.get(entityClass, id);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Retrieves all entities of type T from the database.
     * <p>
     *     This method opens a new Hibernate session and executes an HQL query
     *     to select all records from the entity's table.
     * </p>
     * @return a list containing all entities found
     * @throws DatabaseException if the operation fails
     */
    @Override
    public List<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // e.g. "from User" in Hibernate translates to the following sql code: "SELECT * FROM Users"
            return session.createQuery("from " + entityClass.getName(), entityClass).list();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
