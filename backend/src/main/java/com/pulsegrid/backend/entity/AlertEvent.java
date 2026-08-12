package com.pulsegrid.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "alert_event")
public class AlertEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sensorReadingId;

    private String deviceId;

    private String alertType;

    private String message;

    private double actualValue;

    private double thresholdValue;

    private Instant createdAt;

    private boolean acknowledged;

    protected AlertEvent() {
    }

    public AlertEvent(
            Long sensorReadingId,
            String deviceId,
            String alertType,
            String message,
            double actualValue,
            double thresholdValue,
            Instant createdAt
    ) {
        this.sensorReadingId = sensorReadingId;
        this.deviceId = deviceId;
        this.alertType = alertType;
        this.message = message;
        this.actualValue = actualValue;
        this.thresholdValue = thresholdValue;
        this.createdAt = createdAt;
        this.acknowledged = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSensorReadingId() {
        return sensorReadingId;
    }

    public void setSensorReadingId(Long sensorReadingId) {
        this.sensorReadingId = sensorReadingId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getActualValue() {
        return actualValue;
    }

    public void setActualValue(double actualValue) {
        this.actualValue = actualValue;
    }

    public double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
}