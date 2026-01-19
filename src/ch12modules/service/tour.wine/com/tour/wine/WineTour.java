package com.tour.wine;

import com.tour.api.Tour;
import com.tour.api.TourDetails;

/**
 * SERVICE PROVIDER - Another implementation of Tour
 */
public class WineTour implements Tour {

    public WineTour() {
    }

    @Override
    public String getName() {
        return "Vineyard Wine Tasting Tour";
    }

    @Override
    public double getPrice() {
        return 89.99;
    }

    @Override
    public TourDetails getDetails() {
        return new TourDetails(
                "Visit three local vineyards and taste award-winning wines",
                4,
                20,
                "Easy"
        );
    }
}
