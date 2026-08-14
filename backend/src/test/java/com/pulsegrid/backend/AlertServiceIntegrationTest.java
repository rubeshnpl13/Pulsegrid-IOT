package com.pulsegrid.backend;

import com.pulsegrid.backend.entity.AlertEvent;
import com.pulsegrid.backend.entity.SensorReading;
import com.pulsegrid.backend.repository.AlertEventRepository;
import com.pulsegrid.backend.repository.SensorReadingRepository;
import com.pulsegrid.backend.service.AlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(AlertService.class)
class AlertServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private AlertService alertService;

    @Autowired
    private AlertEventRepository alertEventRepository;

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    @Test
    void shouldDeduplicateHighTemperatureAlerts() {
        SensorReading firstHighReading =
                sensorReadingRepository.save(
                        new SensorReading(
                                "sensor-01",
                                29.0,
                                50.0,
                                "ONLINE",
                                Instant.now()
                        )
                );

        List<AlertEvent> firstAlerts =
                alertService.evaluate(firstHighReading);

        assertThat(firstAlerts).hasSize(1);
        assertThat(firstAlerts.get(0).getAlertType())
                .isEqualTo("TEMPERATURE_HIGH");

        SensorReading secondHighReading =
                sensorReadingRepository.save(
                        new SensorReading(
                                "sensor-01",
                                29.5,
                                52.0,
                                "ONLINE",
                                Instant.now()
                        )
                );

        List<AlertEvent> secondAlerts =
                alertService.evaluate(secondHighReading);

        assertThat(secondAlerts).isEmpty();

        assertThat(alertEventRepository.findAll())
                .hasSize(1);

        SensorReading normalReading =
                sensorReadingRepository.save(
                        new SensorReading(
                                "sensor-01",
                                25.0,
                                50.0,
                                "ONLINE",
                                Instant.now()
                        )
                );

        alertService.evaluate(normalReading);

        AlertEvent storedAlert =
                alertEventRepository.findAll()
                        .get(0);

        assertThat(storedAlert.isResolved())
                .isTrue();
    }
}