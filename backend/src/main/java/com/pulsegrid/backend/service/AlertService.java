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
            AlertEvent temperatureAlert = new AlertEvent(
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

            createdAlerts.add(
                    alertEventRepository.save(temperatureAlert)
            );
        }

        if (reading.getHumidity() > HUMIDITY_LIMIT) {
            AlertEvent humidityAlert = new AlertEvent(
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

            createdAlerts.add(
                    alertEventRepository.save(humidityAlert)
            );
        }

        return createdAlerts;
    }

    public List<AlertEvent> findAll() {
        return alertEventRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<AlertEvent> findActive() {
        return alertEventRepository
                .findByAcknowledgedFalseOrderByCreatedAtDesc();
    }

    public Optional<AlertEvent> acknowledge(Long id) {
        return alertEventRepository.findById(id)
                .map(alert -> {
                    alert.setAcknowledged(true);
                    return alertEventRepository.save(alert);
                });
    }
}