package org.example.ahd.controller;

import org.example.ahd.domain.Coordinates;
import org.example.ahd.domain.Hazard;
import org.example.ahd.domain.HazardStatus;
import org.example.ahd.dto.HazardDetectionRequest;
import org.example.ahd.dto.HazardNotificationResourceHandler;
import org.example.ahd.exceptions.DatabaseException;
import org.example.ahd.exceptions.ValidationException;
import org.example.ahd.service.HazardService;
import org.example.ahd.utils.Observer.Observer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;


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
public class HazardController implements Observer {
    private final HazardService hazardService;
    private final SimpMessagingTemplate messagingTemplate;
    @Value("${file.upload-dir}")
    private String IMAGE_DIR;
    ExecutorService pool;
    /**
     * Constructs the HazardController with the required service.
     *
     * @param hazardService the service handling hazard manipulation logic
     * @param messagingTemplate the template for sending WebSocket messages
     */
    public HazardController(HazardService hazardService, SimpMessagingTemplate messagingTemplate) {
        this.hazardService = hazardService;
        this.messagingTemplate = messagingTemplate;
        hazardService.addObserver(this);
        // pool = Executors.newFixedThreadPool(10);
        pool = Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * Handles {@link Hazard} update requests.
     * <p>
     *     Receives a {@link Hazard} object, validates it, and updates hazard.
     * </p>
     *
     * @param hazard the hazard entity to be updated
     * @return a {@link ResponseEntity} with a success message or error details
     */
    @PutMapping("/update")
    public CompletableFuture<ResponseEntity<?>> updateHazard(@RequestBody Hazard hazard){
        return CompletableFuture.supplyAsync(() -> {
            try{
                hazardService.updateHazard(hazard);
                return ResponseEntity.ok("Hazard updated successfully");
            } catch (ValidationException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during hazard update: " + e.getMessage());
            }
        }, pool);
    }

    /**
     * Handles {@link Hazard} query requests by {@link HazardStatus}.
     *
     * @param status the status of the requested hazards.
     * @return a {@link ResponseEntity} with a list of hazards or error details
     */
    @GetMapping("/getHazardListByStatus")
    public ResponseEntity<?> getHazardsByStatus(@RequestParam HazardStatus status){
        try{
            List<Hazard> hazards = hazardService.findHazardsByStatus(status);
            return ResponseEntity.ok(hazards);
        } catch (DatabaseException e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error: " + e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Handles {@link HazardDetectionRequest} objects sent from the Python server.
     *
     * @param detectionRequest the {@link HazardDetectionRequest} that is a DTO of {@link Hazard}, but also includes the image
     * @return a {@link ResponseEntity} with a success message or error details
     */
    @PostMapping("/detect")
    public CompletableFuture<ResponseEntity<?>> detectHazard(@ModelAttribute HazardDetectionRequest detectionRequest){
        return CompletableFuture.supplyAsync(() -> {
            try{
                System.out.println("Label:" + detectionRequest.getLabel());
                System.out.println("Confidence: " + detectionRequest.getConfidence());
                System.out.println("Coords:" + detectionRequest.getCoord_x() + " " + detectionRequest.getCoord_y());

                String imagePath = saveImage(detectionRequest.getImage());

                Coordinates coordinates = new Coordinates(
                        detectionRequest.getCoord_x(),
                        detectionRequest.getCoord_y()
                );

                Hazard newHazard = new Hazard(
                        coordinates,
                        LocalDateTime.now(),
                        HazardStatus.DETECTED,
                        "Detected: " + detectionRequest.getLabel() + " with confidence: " + detectionRequest.getConfidence(),
                        imagePath
                );

                hazardService.addHazard(newHazard);

                return ResponseEntity.ok("Hazard received and saved successfully");
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during hazard detection: " + e.getMessage());
            }
        }, pool);
    }

    /**
     * Sends notifications to frontend via WebSockets.
     * <p>
     *     This function is automatically called when a {@link Hazard} is inserted
     *     or modified in the database via Observer pattern.
     * </p>
     * @param hazard the {@link HazardDetectionRequest} that is a DTO of {@link Hazard}, but also includes the image
     */
    public void sendHazardToFrontend(HazardNotificationResourceHandler hazard) {
        CompletableFuture.runAsync(() -> {
            messagingTemplate.convertAndSend("/topic/hazards", hazard);
        }, pool);
    }

    /**
     * <p>
     *     Part of Observer pattern. This class is subscribed to the {@link HazardService}.
     * </p>
     * @param object the object to be sent to the frontend
     */
    @Override
    public void doUpdate(Object object) {
        if (object instanceof Hazard) {
            Hazard hazard = (Hazard) object;
            HazardNotificationResourceHandler notification = new HazardNotificationResourceHandler(hazard);
            sendHazardToFrontend(notification);
        }
    }

    // TODO manage to many images case(delete old images)
    //TODO Thread this shit
    private String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path uploadPath = Paths.get(IMAGE_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        return filePath.toAbsolutePath().toString();
    }
}
