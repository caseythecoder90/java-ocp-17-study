/**
 * MODULE: tour.api
 * =================
 *
 * Contains the Service Provider Interface (SPI), referenced classes,
 * and the Service Locator.
 *
 * DIRECTIVES:
 * - exports: Makes SPI package available to other modules
 * - uses: Required for ServiceLoader to find implementations
 */
module tour.api {

    // Export the SPI package so other modules can:
    // - Implement Tour interface (providers)
    // - Use TourDetails (referenced class)
    // - Call TourFinder (service locator)
    exports com.tour.api;

    // REQUIRED: Tells ServiceLoader to look for Tour implementations
    // Without this, ServiceLoader.load(Tour.class) returns empty!
    uses com.tour.api.Tour;
}
