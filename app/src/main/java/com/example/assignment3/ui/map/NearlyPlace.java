package com.example.assignment3.ui.map;

import com.google.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;

// version 1.0.3 Hongyu Yang
public class NearlyPlace {
    public static PlacesSearchResponse run(Double latitude, Double longitude) {
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCXFqRRFtTpVqba8_b6AHnmMqktyMwItMM")
                .build();
        LatLng location = new LatLng(latitude, longitude);
        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000)
                    .rankby(RankBy.PROMINENCE)
                    .keyword("gym")
                    .language("en")
                    .type(PlaceType.GYM)
                    .await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        } finally {
            return request;
        }
    }

}
