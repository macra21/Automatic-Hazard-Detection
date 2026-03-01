package Domain;

import java.time.LocalDateTime;

/**
 * Represents the Hazard entity within the application domain.
 * Includes basic information such as an unique identifier,
 * {@link Location} information, date and time of
 * the discovery{@link HazardStatus} and a short description.
 */
public class Hazard {
    private Integer ID;
    private Location location;
    private LocalDateTime date;
    private HazardStatus status;
    private String description;

    /**
     * Constructs a fully initialized Hazard object.
     * <p>
     *     This constructor is typically when the hazard is retrieved from
     *     the database and already has assigned identifier.
     * </p>
     * @param ID the unique identifier of the hazard
     * @param location the {@link Location} information of the hazard
     * @param status the {@link HazardStatus} og the hazard
     * @param description the description of the hazard
     */
    public Hazard(Integer ID, Location location, LocalDateTime date, HazardStatus status, String description) {
        this.ID = ID;
        this.location = location;
        this.date = date;
        this.status = status;
        this.description = description;
    }

    /**
     * Constructs a Hazard object without an identifier.
     * <p>
     *     This constructor is typically used before the hazard is added to
     *     the database and has no ID. The database will automatically assign
     *     an ID to the hazard if there are no errors.
     * </p>
     * @param location the {@link Location} information of the hazard
     * @param status the {@link HazardStatus} og the hazard
     * @param description the description of the hazard
     */
    public Hazard(Location location, LocalDateTime date, HazardStatus status, String description) {
        this.location = location;
        this.date = date;
        this.status = status;
        this.description = description;
    }

    /**
     * Returns the ID of the hazard.
     * @return the hazard's ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * Sets or updates the ID of the hazard.
     * @param ID the new ID of the hazard
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * Returns the {@link Location} of the hazard.
     * @return the hazard's location
     */
    public Coordinates getLocation() {
        return location;
    }

    /**
     * Sets or updates the {@link Location} of the hazard.
     * @param location the new location of the hazard
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns the date and time of the hazard's creation.
     * @return the date and time of the hazard
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets or updates the date and time of the hazard's creation.
     * @param date the new date and time of the hazard
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the {@link HazardStatus} of the hazard.
     * @return the hazard's status
     */
    public HazardStatus getStatus() {
        return status;
    }

    /**
     * Sets or updates the {@link HazardStatus} of the hazard
     * @param status the new status of the hazard
     */
    public void setStatus(HazardStatus status) {
        this.status = status;
    }

    /**
     * Returns the description of the hazard.
     * @return the hazard's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets or updates the description of the hazard.
     * @param description the new description of the hazard
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
