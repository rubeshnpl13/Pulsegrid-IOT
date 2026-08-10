package com.pulsegrid.backend.service;

import com.pulsegrid.backend.entity.SensorReading;
import com.pulsegrid.backend.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(
            SensorReadingRepository sensorReadingRepository
    ) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public SensorReading save(SensorReading reading) {
        return sensorReadingRepository.save(reading);
    }
}