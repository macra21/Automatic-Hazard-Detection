package org.example.ahd.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents the Hazard entity within the application domain.
 * Includes basic information such as an unique identifier,
 * {@link Location} information, date and time of
 * the discovery{@link HazardStatus} and a short description.
 */
@Entity
@Table(name = "Hazards")
public class Hazard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Embedded // Basically unfolds the class into separate columns within the Hazards table
    private Location location;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private HazardStatus status;

    private String description;
    private String imagePath; // Path to the corresponding image

    @ManyToOne
    @JoinColumn(name = "atc_user_id")
    private User atcUser;

    @ManyToOne
    @JoinColumn(name = "cleanup_user_id")
    private User cleanupUser;

    /**
     * Constructor without arguments required by Hibernate.
     */
    public Hazard() {}

    /**
     * Constructs a fully initialized Hazard object.
     * <p>
     *     This constructor is typically when the hazard is retrieved from
     *     the database and already has assigned identifier.
     * </p>
     * <p>
     *     <b>Note:</b> This constructor was implemented for JDBC compatibility and is preserved for that purpose.
     * </p>
     * @param ID the unique identifier of the hazard
     * @param location the {@link Location} information of the hazard
     * @param status the {@link HazardStatus} og the hazard
     * @param description the description of the hazard
     */
    public Hazard(Integer ID, Location location, LocalDateTime date, HazardStatus status, String description, String imagePath) {
        this.ID = ID;
        this.location = location;
        this.date = date;
        this.status = status;
        this.description = description;
        this.imagePath = imagePath;
    }

    /**
     * Constructs a Hazard object without an identifier.
     * <p>
     *     This constructor is typically used before the hazard is added to
     *     the database and has no ID. The database will automatically assign
     *     an ID to the hazard if there are no errors.
     * </p>
     * <p>
     *     <b>Note:</b> This constructor was implemented for JDBC compatibility and is preserved for that purpose.
     * </p>
     * @param location the {@link Location} information of the hazard
     * @param status the {@link HazardStatus} og the hazard
     * @param description the description of the hazard
     */
    public Hazard(Location location, LocalDateTime date, HazardStatus status, String description, String imagePath) {
        this.location = location;
        this.date = date;
        this.status = status;
        this.description = description;
        this.imagePath = imagePath;
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
    public Location getLocation() {
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

    /**
     * Returns the ATC user who confirmed the hazard.
     * @return the ATC user
     */
    public User getAtcUser() {
        return atcUser;
    }

    /**
     * Sets or updates the ATC user who confirmed the hazard.
     * @param atcUser the new ATC user
     */
    public void setAtcUser(User atcUser) {
        this.atcUser = atcUser;
    }

    /**
     * Returns the cleanup user who cleared the hazard.
     * @return the cleanup user
     */
    public User getCleanupUser() {
        return cleanupUser;
    }

    /**
     * Sets or updates the cleanup user who cleared the hazard.
     * @param cleanupUser the new cleanup user
     */
    public void setCleanupUser(User cleanupUser) {
        this.cleanupUser = cleanupUser;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
