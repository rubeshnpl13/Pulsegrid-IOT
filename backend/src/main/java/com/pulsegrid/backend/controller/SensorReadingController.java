package com.pulsegrid.backend.controller;

import com.pulsegrid.backend.entity.SensorReading;
import com.pulsegrid.backend.service.SensorReadingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    public SensorReadingController(
            SensorReadingService sensorReadingService
    ) {
        this.sensorReadingService = sensorReadingService;
    }

    @GetMapping
    public List<SensorReading> getAllReadings() {
        return sensorReadingService.findAll();
    }

    @GetMapping("/latest")
    public ResponseEntity<SensorReading> getLatestReading() {
        return sensorReadingService.findLatest()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}