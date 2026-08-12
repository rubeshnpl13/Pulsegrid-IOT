package com.pulsegrid.backend.service;

import com.pulsegrid.backend.entity.SensorReading;
import com.pulsegrid.backend.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final AlertService alertService;

    public SensorReadingService(
            SensorReadingRepository sensorReadingRepository,
            AlertService alertService
    ) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.alertService = alertService;
    }

    public SensorReading save(SensorReading reading) {
        SensorReading savedReading =
                sensorReadingRepository.save(reading);

        alertService.evaluate(savedReading);

        return savedReading;
    }

    public List<SensorReading> findAll() {
        return sensorReadingRepository.findAllByOrderByTimestampDesc();
    }

    public Optional<SensorReading> findLatest() {
        return sensorReadingRepository.findFirstByOrderByTimestampDesc();
    }
}