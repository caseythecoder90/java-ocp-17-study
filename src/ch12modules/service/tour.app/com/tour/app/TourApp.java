package com.tour.app;

import com.tour.api.Tour;
import com.tour.api.TourDetails;
import com.tour.api.TourFinder;

import java.util.List;

/**
 * SERVICE CONSUMER
 *
 * Uses TourFinder (service locator) to discover Tour implementations.
 * Does NOT know about HikingTour or WineTour directly!
 */
public class TourApp {

    public static void main(String[] args) {
        System.out.println("=== Tour Application (Service Consumer) ===\n");

        // Use service locator - consumer doesn't know implementation classes!
        List<Tour> tours = TourFinder.findAllTours();

        if (tours.isEmpty()) {
            System.out.println("No tours available.");
            return;
        }

        System.out.println("Available Tours:");
        System.out.println("-".repeat(50));

        for (Tour tour : tours) {
            System.out.println("\nTour: " + tour.getName());
            System.out.println("Price: $" + tour.getPrice());

            TourDetails details = tour.getDetails();
            System.out.println("Description: " + details.description());
            System.out.println("Duration: " + details.durationHours() + " hours");
            System.out.println("Max Group Size: " + details.maxGroupSize());
            System.out.println("Difficulty: " + details.difficulty());
        }

        System.out.println("\n" + "-".repeat(50));
        System.out.println("Total tours found: " + tours.size());

        // Demonstrate stream() method
        System.out.println("\n=== Using ServiceLoader.stream() ===\n");
        TourFinder.listAvailableTourTypes();
    }
}
