package org.example.ahd.validators;

import org.example.ahd.domain.Hazard;
import org.example.ahd.domain.HazardStatus;
import org.example.ahd.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Validator class for the {@link Hazard} entity.
 * Implements the {@link IValidator} interface.
 */
@Component
public class HazardValidator implements IValidator<Hazard>{
    /**
     * Validates an {@link Hazard} entity.
     * @param entity the hazard to be validated
     * @throws org.example.ahd.exceptions.ValidationException if the hazard does not comply with the business logic.
     */
    @Override
    public void validate(Hazard entity){
        StringBuilder errors = new StringBuilder();

        if (entity.getLocation() == null){
            errors.append("Location is required!\n");
        }
        // TODO: Validate the location and coordinates

        if (entity.getDate() == null){
            errors.append("Date is required!\n");
        }
        else if(entity.getDate().isAfter(LocalDateTime.now())){
            errors.append("Date cannot be in the future!\n");
        }

        if (entity.getStatus() == null){
            errors.append("Status is required!\n");
        }
        else if (!entity.getStatus().equals(HazardStatus.DETECTED) &&
                 !entity.getStatus().equals(HazardStatus.CONFIRMED) &&
                 !entity.getStatus().equals(HazardStatus.DISMISSED) &&
                 !entity.getStatus().equals(HazardStatus.CLEARED) &&
                 !entity.getStatus().equals(HazardStatus.FALSE_ALARM)){
            errors.append("Status must be 'DETECTED', 'CONFIRMED', 'DISMISSED', 'CLEARED' or 'FALSE_ALARM'!\n");
        }

        // TODO: Validate by assigned atcUser and cleanupUser (maybe in Service)

        if (!errors.isEmpty()){
            throw new ValidationException(errors.toString());
        }
    }
}
