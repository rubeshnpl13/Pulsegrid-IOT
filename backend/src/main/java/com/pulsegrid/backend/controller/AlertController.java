package com.pulsegrid.backend.controller;

import com.pulsegrid.backend.entity.AlertEvent;
import com.pulsegrid.backend.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:5173")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<AlertEvent> getAllAlerts() {
        return alertService.findAll();
    }

    @GetMapping("/active")
    public List<AlertEvent> getActiveAlerts() {
        return alertService.findActive();
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<AlertEvent> acknowledgeAlert(
            @PathVariable Long id
    ) {
        return alertService.acknowledge(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}