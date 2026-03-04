package org.example.ahd.controller;

import org.example.ahd.domain.Hazard;
import org.example.ahd.dto.HazardDetectionRequest;
import org.example.ahd.exceptions.ValidationException;
import org.example.ahd.service.HazardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * REST Controller for handling hazard updates.
 * <p>
 *     This controller exposes endpoints for hazard updates(specifically status updates).
 *     It acts as an interface between the frontend (Angular) and the backend services.
 * </p>
 */
@RestController
@RequestMapping("/api/hazards")
@CrossOrigin(origins = "*")
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

    @PostMapping("/detect")
    public ResponseEntity<?> detectHazard(@ModelAttribute HazardDetectionRequest detectionRequest){
        try{
            System.out.println("Label:" + detectionRequest.getLabel());
            System.out.println("Confidence: " + detectionRequest.getConfidence());
            System.out.println("Coords:" + detectionRequest.getCoord_x() + " " + detectionRequest.getCoord_y());
            // Do something with this🙎🏿‍♂️
            return ResponseEntity.ok("Hazard received successfully");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during hazard detection: " + e.getMessage());
        }
    }
}
