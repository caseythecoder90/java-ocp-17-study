package com.tour.api;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * SERVICE LOCATOR
 *
 * Uses ServiceLoader to find and load Tour implementations.
 * This is PART OF the service.
 *
 * The module containing this class needs:
 *   uses com.tour.api.Tour;
 */
public class TourFinder {

    /**
     * METHOD 1: Using load() and iterating
     *
     * ServiceLoader implements Iterable, so you can use for-each loop.
     */
    public static List<Tour> findAllTours() {
        List<Tour> tours = new ArrayList<>();

        ServiceLoader<Tour> loader = ServiceLoader.load(Tour.class);

        for (Tour tour : loader) {
            tours.add(tour);
        }

        return tours;
    }

    /**
     * METHOD 2: Using stream()
     *
     * Returns Stream<Provider<Tour>> - must call get() on each Provider!
     */
    public static List<Tour> findAllToursWithStream() {
        return ServiceLoader.load(Tour.class)
                .stream()
                .map(ServiceLoader.Provider::get)  // Must call get()!
                .collect(Collectors.toList());
    }

    /**
     * Using Provider.type() for lazy inspection without instantiation
     */
    public static void listAvailableTourTypes() {
        System.out.println("Available Tour implementations:");
        ServiceLoader.load(Tour.class)
                .stream()
                .map(ServiceLoader.Provider::type)
                .forEach(clazz -> System.out.println("  - " + clazz.getName()));
    }
}
