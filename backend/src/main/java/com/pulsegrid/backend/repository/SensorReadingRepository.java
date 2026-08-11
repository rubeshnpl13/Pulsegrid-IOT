package com.pulsegrid.backend.repository;

import com.pulsegrid.backend.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findAllByOrderByTimestampDesc();

    Optional<SensorReading> findFirstByOrderByTimestampDesc();
}