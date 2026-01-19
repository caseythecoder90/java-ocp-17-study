/**
 * MODULE: tour.hiking
 * ====================
 *
 * SERVICE PROVIDER - provides HikingTour implementation.
 *
 * DIRECTIVES:
 * - requires: Depend on SPI module
 * - provides...with: Declare the implementation
 *
 * NOTE: Implementation does NOT need to be exported!
 */
module tour.hiking {

    requires tour.api;

    provides com.tour.api.Tour
        with com.tour.hiking.HikingTour;
}
