package org.example.ahd.domain;

import jakarta.persistence.MappedSuperclass;

/**
 * Helper class for coordinates management.
 * Includes information such as latitude and longitude.
 * <p>
 *     Designed to be used within the {@link Location} class.
 * </p>
 */
@MappedSuperclass // It's parameters will be used by the child class(Location)
public class Coordinates {
    private Double latitude;
    private Double longitude;

    /**
     * Constructor without arguments required by Hibernate.
     */
    public Coordinates() {}

    /**
     * Construct a fully initialized Coordinates object.
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude of the coordinates.
     * @return the coordinates' latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets or updates the latitude of the coordinates.
     * @param latitude the new latitude of the coordinates
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude of the coordinates.
     * @return the coordinates' longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets or updates the longitude of the coordinates.
     * @param longitude the new longitude of the coordinates
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
