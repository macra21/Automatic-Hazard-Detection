package Repository;

import Domain.Hazard;
import Domain.HazardStatus;
import Exceptions.DatabaseException;
import Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

/**
 * Repository implementation for the {@link Hazard} entity.
 * <p>
 *     Extends the {@link AbstractRepository} to provide standard CRUD operations
 *     and adds specific methods for querying hazards.
 * </p>
 */
public class HazardRepository extends AbstractRepository<Hazard, Integer> {

    /**
     * Constructs a new HazardRepository.
     * Initializes the repository with the {@link Hazard} class type.
     */
    public HazardRepository() {
        super(Hazard.class);
    }

    /**
     * Finds all hazards with a specific status.
     * <p>
     *     This method executes an HQL query to retrieve all hazards that match
     *     the given {@link HazardStatus}.
     * </p>
     * @param status the {@link HazardStatus} to filter by
     * @return a list of {@link Hazard} objects with the specified status
     * @throws DatabaseException if the operation fails
     */
    public List<Hazard> findByStatus(HazardStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Hazard> query = session.createQuery("FROM Hazard WHERE status = :status", Hazard.class);
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Finds all hazards confirmed by a specific ATC user.
     * <p>
     *     This method executes an HQL query to retrieve all hazards where the
     *     {@code atcUser} field matches the given user ID.
     * </p>
     * @param userId the ID of the ATC user
     * @return a list of {@link Hazard} objects confirmed by the specified user
     * @throws DatabaseException if the operation fails
     */
    public List<Hazard> findByAtcUser(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Hazard> query = session.createQuery("FROM Hazard WHERE atcUser.id = :userId", Hazard.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Finds all hazards cleared by a specific cleanup user.
     * <p>
     *     This method executes an HQL query to retrieve all hazards where the
     *     {@code cleanupUser} field matches the given user ID.
     * </p>
     * @param userId the ID of the cleanup user
     * @return a list of {@link Hazard} objects cleared by the specified user
     * @throws DatabaseException if the operation fails
     */
    public List<Hazard> findByCleanupUser(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Hazard> query = session.createQuery("FROM Hazard WHERE cleanupUser.id = :userId", Hazard.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
