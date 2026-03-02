package Domain;

import jakarta.persistence.Embeddable;

/**
 * Helper class to help with positioning information of a {@link Hazard}.
 * <p>
 *     Contains the necessary location information to manage a Hazard
 *     such as {@link Coordinates}, height (pre established height of the
 *     drone's flight) and width, length and the corner pixels of the
 *     rectangle that contains the hazard in a photo.
 * </p>
 */
@Embeddable // Embedded class in Hazard
public class Location extends Coordinates {
    private static final Double height = 15.0; // Pre established height of the drone's flight
    private Double width;
    private Double length;
    private Double pixelTopLeft;

    /**
     * Constructor without arguments required by Hibernate.
     */
    public Location() {}

    /**
     * Constructs a fully initialized location for a {@link Hazard}.
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     * @param width the width of the box that contains the hazard
     * @param length the length of the box that contains the hazard
     * @param pixelTopLeft the top left pixel of the box that contains the hazard (relative to the top left (0,0)pixel of the original photo
     */
    public Location(Double latitude, Double longitude, Double width, Double length, Double pixelTopLeft) {
        super(latitude, longitude);
        this.width = width;
        this.length = length;
        this.pixelTopLeft = pixelTopLeft;
    }

    /**
     * Returns the width of the box that contains the hazard.
     * @return the width of the box that contains the hazard
     */
    public Double getWidth() {
        return width;
    }

    /**
     * Sets or updates the width of the box that contains the hazard.
     * @param width the new width of the box that contains the hazard
     */
    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * Returns the length of the box that contains the hazard.
     * @return the length of the box that contains the hazard
     */
    public Double getLength() {
        return length;
    }

    /**
     * Sets or updates the length of the box that contains the hazard.
     * @param length the new length of the box that contains the hazard
     */
    public void setLength(Double length) {
        this.length = length;
    }

    /**
     * Returns the top left pixel of the box that contains the hazard.
     * @return the top left of the box that contains the hazard
     */
    public Double getPixelTopLeft() {
        return pixelTopLeft;
    }

    /**
     * Sets or updates the top left pixel of the box that contains the hazard.
     * @param pixelTopLeft the new top left pixel of the box that contains the hazard
     */
    public void setPixelTopLeft(Double pixelTopLeft) {
        this.pixelTopLeft = pixelTopLeft;
    }
}
