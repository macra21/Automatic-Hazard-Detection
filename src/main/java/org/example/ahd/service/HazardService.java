package org.example.ahd.service;

import org.example.ahd.domain.Hazard;
import org.example.ahd.domain.HazardStatus;
import org.example.ahd.domain.User;
import org.example.ahd.repository.HazardRepository;
import org.example.ahd.validators.IValidator;

import java.util.List;

/**
 * Service class for {@link Hazard} entity.
 * <p>
 * Includes CRUD operations with the Database and
 * more complex functionalities.
 * </p>
 */
public class HazardService {
    private final HazardRepository hazardRepository;
    private final IValidator<Hazard> hazardValidator;

    /**
     * Constructs a fully initialized Hazard Service
     * @param hazardRepository the repository used by this service
     * @param hazardValidator the validator used by this service
     */
    public HazardService(HazardRepository hazardRepository, IValidator<Hazard> hazardValidator) {
        this.hazardRepository = hazardRepository;
        this.hazardValidator = hazardValidator;
    }

    /**
     * Saves a {@link Hazard} to the database.
     *
     * @param hazard the hazard to be saved
     * @throws org.example.ahd.exceptions.ValidationException if the Hazard is invalid
     * @throws org.example.ahd.exceptions.DatabaseException if the database throws an error(e.g. the hazard is already in the database, etc.)
     */
    public void addHazard(Hazard hazard) {
        hazardValidator.validate(hazard);
        hazardRepository.add(hazard);
    }

    /**
     * Updates a {@link Hazard} in the database.
     *
     * @param hazard the hazard to be updated in the database
     * @throws org.example.ahd.exceptions.ValidationException if the Hazard is invalid
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    public void updateHazard(Hazard hazard) {
        hazardValidator.validate(hazard);
        hazardRepository.update(hazard);
    }

    /**
     * Deletes a {@link Hazard} in the database based on its ID.
     *
     * @param ID the id of the hazard to be deleted in the database
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails(e.g. the hazard was not found, etc.)
     */
    public void deleteHazard(Integer ID) {
        hazardRepository.deleteById(ID);
    }

    /**
     * Returns a {@link Hazard} by its ID.
     *
     * @param ID the ID of the searched hazard
     * @return the hazard if found or null if not found
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    public Hazard findHazardById(Integer ID) {
        return hazardRepository.findById(ID);
    }

    /**
     * Returns all the hazards from the database.
     * <p>
     * <strong>Warning: </strong> Use this function carefully because the database can be quite large.
     * </p>
     *
     * @return a list of all the hazards
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    public List<Hazard> getAllHazards() {
        return hazardRepository.getAll();
    }

    /**
     * Returns a List of {@link Hazard} based on its {@link HazardStatus}
     * @param status the status of the searched hazard
     * @return the list of hazards
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    public List<Hazard> findHazardsByStatus(HazardStatus status) {
        return hazardRepository.findByStatus(status);
    }

    /**
     * Returns a List of {@link Hazard} based on its ATC User ID.
     * @param userId the ATC User ID of the searched hazard
     * @return the list of hazards
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    public List<Hazard> findHazardsByATCUserId(Integer userId) {
        return hazardRepository.findByAtcUser(userId);
    }

    /**
     * Returns a List of {@link Hazard} based on its Cleanup User ID.
     * @param userId the Cleanup User ID of the searched hazard
     * @return the list of hazards
     * @throws org.example.ahd.exceptions.DatabaseException if the database operation fails
     */
    public List<Hazard> findHazardsByCleanupUserId(Integer userId) {
        return hazardRepository.findByCleanupUser(userId);
    }
}
