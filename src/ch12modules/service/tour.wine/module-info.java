/**
 * MODULE: tour.wine
 * ==================
 *
 * SERVICE PROVIDER - provides WineTour implementation.
 *
 * Same pattern as tour.hiking - demonstrates multiple providers.
 */
module tour.wine {

    requires tour.api;

    provides com.tour.api.Tour
        with com.tour.wine.WineTour;
}
