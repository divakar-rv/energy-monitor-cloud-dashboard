package com.energy.monitor.controller;

import com.energy.monitor.dto.ReadingRequest;
import com.energy.monitor.model.Reading;
import com.energy.monitor.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * Same layered pattern as your BookingController: a thin controller that
 * delegates to the repository. Three responsibilities here:
 *   1. POST /api/readings   -> ESP32 sends a new reading
 *   2. GET  /api/readings   -> React dashboard fetches history on page load
 *   3. broadcasting the new reading over WebSocket for instant live updates
 */
@RestController
@RequestMapping("/api/readings")
public class ReadingController {

    private final ReadingRepository readingRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ReadingController(ReadingRepository readingRepository,
                              SimpMessagingTemplate messagingTemplate) {
        this.readingRepository = readingRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Called by the ESP32 every few seconds.
     * Example body: { "voltage": 231.2, "current": 1.85 }
     */
    @PostMapping
    public Reading receiveReading(@RequestBody ReadingRequest request) {
        double power = request.getVoltage() * request.getCurrent();

        Reading reading = new Reading(
                request.getVoltage(),
                request.getCurrent(),
                power,
                Instant.now()
        );
        Reading saved = readingRepository.save(reading);

        // Push it live to any connected dashboard immediately.
        // Anything sent here shows up on the frontend without it needing
        // to ask for it — this is the WebSocket "broadcast" in action.
        messagingTemplate.convertAndSend("/topic/readings", saved);

        return saved;
    }

    /**
     * Called by the React dashboard once, on page load, to fill in
     * the chart with recent history before live updates start arriving.
     */
    @GetMapping
    public List<Reading> getRecentReadings() {
        return readingRepository.findRecent();
    }
}
