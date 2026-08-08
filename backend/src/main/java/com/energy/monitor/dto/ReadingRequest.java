package com.energy.monitor.dto;

/**
 * What the ESP32 actually sends us: just voltage and current.
 * We calculate power (watts) ourselves in the controller — keeps the
 * ESP32 side simple and the "real" calculation in one place.
 *
 * This is a DTO (Data Transfer Object) — same idea as your AuthDtos /
 * BookingDtos in the ticket booking backend: a plain class shaped
 * exactly like the incoming JSON, kept separate from the database
 * entity (Reading.java) so the two can change independently.
 */
public class ReadingRequest {
    private Double voltage;
    private Double current;

    public ReadingRequest() {
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
}
