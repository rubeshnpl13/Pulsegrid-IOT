package com.pulsegrid.backend.repository;

import com.pulsegrid.backend.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
}