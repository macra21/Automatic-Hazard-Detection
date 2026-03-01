package Domain;

/**
 * Helper enum class that contains the status of the {@link Hazard}.
 * <b>Status states:</b>
 * <p>
 * <ul>
 *     <li>DETECTED - The suspected hazard waits to be checked by a human supervisor</il>
 *     <li>CONFIRMED - The hazard was confirmed to be dangerous by a human being supervisor</il>
 *     <li>DISMISSED - Tha hazard was confirmed to be a false alarm by a human supervisor</il>
 *     <li>CLEARED - The hazard wad cleared from the runway by the ground crew after it was confirmed to be dangerous</il>
 *     <li>FALSE_ALARM - The ground crew decided the hazard was a false alarm</li>
 * </ul>
 * </p>
 */
public enum HazardStatus {
    DETECTED,
    CONFIRMED,
    DISMISSED,
    CLEARED,
    FALSE_ALARM
}
