package com.energy.monitor.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * One row = one sensor reading sent by the ESP32.
 *
 * Just like your Event/Seat entities in the booking app, this class
 * maps directly to a database table. JPA creates the table for us
 * automatically based on this class (see application.properties,
 * where ddl-auto=update handles that).
 */
@Entity
@Table(name = "readings")
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Voltage in volts, e.g. 230.5
    private Double voltage;

    // Current in amps, e.g. 2.13
    private Double current;

    // Real power in watts — we calculate this (voltage * current) either
    // on the ESP32 or here in the backend; we'll do it here for simplicity.
    private Double powerWatts;

    // When the reading was received by the server (set automatically)
    private Instant timestamp;

    public Reading() {
    }

    public Reading(Double voltage, Double current, Double powerWatts, Instant timestamp) {
        this.voltage = voltage;
        this.current = current;
        this.powerWatts = powerWatts;
        this.timestamp = timestamp;
    }

    // --- Getters and setters (JPA and Jackson/JSON both need these) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Double getPowerWatts() {
        return powerWatts;
    }

    public void setPowerWatts(Double powerWatts) {
        this.powerWatts = powerWatts;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
