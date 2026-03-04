package org.example.ahd.repository;

import org.example.ahd.domain.Hazard;
import org.example.ahd.domain.HazardStatus;
import org.example.ahd.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository implementation for the {@link Hazard} entity.
 * <p>
 *     Extends the {@link AbstractRepository} to provide standard CRUD operations
 *     and adds specific methods for querying hazards using JPA.
 * </p>
 */
@Repository
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
     * @param status the {@link HazardStatus} to filter by
     * @return a list of {@link Hazard} objects with the specified status
     * @throws DatabaseException if the operation fails
     */
    public List<Hazard> findByStatus(HazardStatus status) {
        try {
            return entityManager.createQuery("SELECT h FROM Hazard h WHERE h.status = :status", Hazard.class)
                    .setParameter("status", status)
                    .getResultList();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Finds all hazards confirmed by a specific ATC user.
     * @param userId the ID of the ATC user
     * @return a list of {@link Hazard} objects confirmed by the specified user
     * @throws DatabaseException if the operation fails
     */
    public List<Hazard> findByAtcUser(Integer userId) {
        try {
            return entityManager.createQuery("SELECT h FROM Hazard h WHERE h.atcUser.id = :userId", Hazard.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Finds all hazards cleared by a specific cleanup user.
     * @param userId the ID of the cleanup user
     * @return a list of {@link Hazard} objects cleared by the specified user
     * @throws DatabaseException if the operation fails
     */
    public List<Hazard> findByCleanupUser(Integer userId) {
        try {
            return entityManager.createQuery("SELECT h FROM Hazard h WHERE h.cleanupUser.id = :userId", Hazard.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
