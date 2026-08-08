package com.energy.monitor.repository;

import com.energy.monitor.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Same pattern as BookingRepository in your ticket booking app.
 * Extending JpaRepository gives us save(), findAll(), findById(), etc.
 * for free — no SQL needed for the basics.
 */
public interface ReadingRepository extends JpaRepository<Reading, Long> {

    // Custom query: get the most recent N readings, newest first.
    // JPQL (the query language here) looks like SQL but works on your
    // Java entities/fields (e.g. "timestamp") rather than raw DB columns.
    @Query("SELECT r FROM Reading r ORDER BY r.timestamp DESC")
    List<Reading> findRecent();
}
