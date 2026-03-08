package org.example.ahd.controller;

import org.example.ahd.domain.ResponseTime;
import org.example.ahd.exceptions.DatabaseException;
import org.example.ahd.service.ResponseTimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/responseTime")
@CrossOrigin("*")
public class ResponseTimeController {
    private final ResponseTimeService responseTimeService;
    ExecutorService pool;

    public ResponseTimeController(ResponseTimeService responseTimeService) {
        this.responseTimeService = responseTimeService;
        pool = Executors.newFixedThreadPool(10);
    }

    @GetMapping("/getResponseTimes")
    public CompletableFuture<ResponseEntity<?>> getResponseTimes(){
        return CompletableFuture.supplyAsync(() -> {
            try{
                List<ResponseTime> responseTimes = responseTimeService.getAllResponseTimes();
                return ResponseEntity.ok(responseTimes);
            } catch (DatabaseException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error: " + e.getMessage());
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during Response Time fetching: " + e.getMessage());
            }
        }, pool);
    }
}
