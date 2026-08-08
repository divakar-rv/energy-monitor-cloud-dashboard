package com.energy.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point. Same pattern as your ticket booking app's main class —
 * this just boots the whole Spring context (web server, database
 * connection, all your beans) when you run the app.
 */
@SpringBootApplication
public class EnergyMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnergyMonitorApplication.class, args);
    }
}
