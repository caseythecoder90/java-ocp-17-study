/**
 * MODULE: tour.app
 * =================
 *
 * SERVICE CONSUMER - uses the Tour service.
 *
 * DIRECTIVES:
 * - requires: Only needs the SPI module!
 *
 * Does NOT need:
 * - 'uses' (service locator has that)
 * - 'requires tour.hiking' or 'requires tour.wine' (loose coupling!)
 */
module tour.app {

    requires tour.api;
}
