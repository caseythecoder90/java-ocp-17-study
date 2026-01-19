package com.tour.api;

/**
 * REFERENCED CLASS
 *
 * This class is PART OF the service because Tour interface
 * uses it as a return type. Must be exported with the SPI.
 */
public record TourDetails(
        String description,
        int durationHours,
        int maxGroupSize,
        String difficulty
) {
}
