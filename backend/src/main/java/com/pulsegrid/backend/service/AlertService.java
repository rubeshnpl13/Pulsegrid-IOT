package com.pulsegrid.backend.service;

import com.pulsegrid.backend.entity.AlertEvent;
import com.pulsegrid.backend.entity.SensorReading;
import com.pulsegrid.backend.repository.AlertEventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private static final double TEMPERATURE_LIMIT = 28.0;
    private static final double HUMIDITY_LIMIT = 65.0;

    private final AlertEventRepository alertEventRepository;

    public AlertService(
            AlertEventRepository alertEventRepository
    ) {
        this.alertEventRepository = alertEventRepository;
    }

    public List<AlertEvent> evaluate(SensorReading reading) {
        List<AlertEvent> createdAlerts = new ArrayList<>();

        if (reading.getTemperature() > TEMPERATURE_LIMIT) {
            createTemperatureAlertIfNeeded(reading)
                    .ifPresent(createdAlerts::add);
        } else {
            resolveAlerts(
                    reading.getDeviceId(),
                    "TEMPERATURE_HIGH"
            );
        }

        if (reading.getHumidity() > HUMIDITY_LIMIT) {
            createHumidityAlertIfNeeded(reading)
                    .ifPresent(createdAlerts::add);
        } else {
            resolveAlerts(
                    reading.getDeviceId(),
                    "HUMIDITY_HIGH"
            );
        }

        return createdAlerts;
    }

    private Optional<AlertEvent> createTemperatureAlertIfNeeded(
            SensorReading reading
    ) {
        List<AlertEvent> existingAlerts =
                alertEventRepository
                        .findAllByDeviceIdAndAlertTypeAndResolvedFalse(
                                reading.getDeviceId(),
                                "TEMPERATURE_HIGH"
                        );

        if (!existingAlerts.isEmpty()) {
            return Optional.empty();
        }

        AlertEvent alert = new AlertEvent(
                reading.getId(),
                reading.getDeviceId(),
                "TEMPERATURE_HIGH",
                "Temperature is high: "
                        + reading.getTemperature()
                        + " °C",
                reading.getTemperature(),
                TEMPERATURE_LIMIT,
                reading.getTimestamp()
        );

        return Optional.of(
                alertEventRepository.save(alert)
        );
    }

    private Optional<AlertEvent> createHumidityAlertIfNeeded(
            SensorReading reading
    ) {
        List<AlertEvent> existingAlerts =
                alertEventRepository
                        .findAllByDeviceIdAndAlertTypeAndResolvedFalse(
                                reading.getDeviceId(),
                                "HUMIDITY_HIGH"
                        );

        if (!existingAlerts.isEmpty()) {
            return Optional.empty();
        }

        AlertEvent alert = new AlertEvent(
                reading.getId(),
                reading.getDeviceId(),
                "HUMIDITY_HIGH",
                "Humidity is high: "
                        + reading.getHumidity()
                        + " %",
                reading.getHumidity(),
                HUMIDITY_LIMIT,
                reading.getTimestamp()
        );

        return Optional.of(
                alertEventRepository.save(alert)
        );
    }

    private void resolveAlerts(
            String deviceId,
            String alertType
    ) {
        List<AlertEvent> unresolvedAlerts =
                alertEventRepository
                        .findAllByDeviceIdAndAlertTypeAndResolvedFalse(
                                deviceId,
                                alertType
                        );

        for (AlertEvent alert : unresolvedAlerts) {
            alert.setResolved(true);
            alertEventRepository.save(alert);
        }
    }

    public List<AlertEvent> findAll() {
        return alertEventRepository
                .findAllByOrderByCreatedAtDesc();
    }

    public List<AlertEvent> findActive() {
        return alertEventRepository
                .findByResolvedFalseAndAcknowledgedFalseOrderByCreatedAtDesc();
    }

    public Optional<AlertEvent> acknowledge(Long id) {
        return alertEventRepository.findById(id)
                .map(alert -> {
                    alert.setAcknowledged(true);
                    return alertEventRepository.save(alert);
                });
    }
}