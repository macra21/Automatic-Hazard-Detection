package org.example.ahd.controller;

import org.example.ahd.domain.Hazard;
import org.example.ahd.domain.User;
import org.example.ahd.exceptions.ValidationException;
import org.example.ahd.service.HazardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling hazard updates.
 * <p>
 *     This controller exposes endpoints for hazard updates(specifically status updates).
 *     It acts as an interface between the frontend (Angular) and the backend services.
 * </p>
 */
@RestController
@RequestMapping("/api/hazards")
@CrossOrigin(origins = "http://localhost:4200")
public class HazardController {
    private final HazardService hazardService;

    /**
     * Constructs the HazardController with the required service.
     * @param hazardService the service handling hazard manipulation logic
     */
    public HazardController(HazardService hazardService) {
        this.hazardService = hazardService;
    }

    /**
     * Handles {@link Hazard} update requests.
     * <p>
     *     Receives a {@link Hazard} object, validates it, and updates hazard.
     * </p>
     * @param hazard the hazard entity to be updated
     * @return a {@link ResponseEntity} with a success message or error details
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateHazard(@RequestBody Hazard hazard){
        try{
            hazardService.updateHazard(hazard);
            return ResponseEntity.ok("Hazard updated successfully");
        } catch (ValidationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during hazard update: " + e.getMessage());
        }
    }
}
