package com.tour.api;

/**
 * SERVICE PROVIDER INTERFACE (SPI)
 *
 * Defines the contract for Tour services.
 * This is PART OF the service.
 */
public interface Tour {

    String getName();

    double getPrice();

    /**
     * TourDetails is a "referenced class" - also part of the service
     * because this interface uses it as a return type.
     */
    TourDetails getDetails();
}
