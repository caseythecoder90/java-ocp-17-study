package com.tour.hiking;

import com.tour.api.Tour;
import com.tour.api.TourDetails;

/**
 * SERVICE PROVIDER - Implementation of Tour
 *
 * Requirements:
 * - Must implement the SPI (Tour)
 * - Must have public no-arg constructor
 * - Does NOT need to be in exported package
 */
public class HikingTour implements Tour {

    public HikingTour() {
        // Public no-arg constructor required for ServiceLoader
    }

    @Override
    public String getName() {
        return "Mountain Hiking Adventure";
    }

    @Override
    public double getPrice() {
        return 149.99;
    }

    @Override
    public TourDetails getDetails() {
        return new TourDetails(
                "Experience breathtaking mountain views on this guided hiking tour",
                6,
                12,
                "Moderate"
        );
    }
}
