package com.pulsegrid.backend.service;

import com.pulsegrid.backend.entity.SensorReading;
import com.pulsegrid.backend.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<SensorReading> findAll() {
        return sensorReadingRepository.findAllByOrderByTimestampDesc();
    }

    public Optional<SensorReading> findLatest() {
        return sensorReadingRepository.findFirstByOrderByTimestampDesc();
    }
}