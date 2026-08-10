package com.pulsegrid.backend;

import com.pulsegrid.backend.entity.SensorReading;
import com.pulsegrid.backend.service.SensorReadingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SensorReadingServiceTest {

    @Autowired
    private SensorReadingService sensorReadingService;

    @Test
    void shouldSaveSensorReading() {
        SensorReading reading = new SensorReading(
                "sensor-01",
                24.2,
                51.0,
                "ONLINE",
                Instant.now()
        );

        SensorReading savedReading =
                sensorReadingService.save(reading);

        assertNotNull(savedReading.getId());
        assertEquals("sensor-01", savedReading.getDeviceId());
    }
}