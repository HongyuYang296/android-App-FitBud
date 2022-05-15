package com.example.assignment3.ui.map;


import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.assignment3.R;
import com.example.assignment3.entity.User;
import com.example.assignment3.viewmodel.UserViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;


import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.PlacesSearchResult;

import java.util.List;
import java.util.Objects;

// version 1.0.1: map fragment --Hongyu
// setup google using google api and add marker to nearly gym and users
public class MapFragment extends Fragment {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        UserViewModel userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(UserViewModel.class);
        Context context = requireActivity().getApplicationContext();

        Bundle bundle = requireActivity().getIntent().getExtras();
        User user = bundle.getParcelable("loginUser");
        String address = user.getAddress();


        // Async map
        Objects.requireNonNull(supportMapFragment).getMapAsync(googleMap -> {
            LatLng latLng = getLocationFromAddress(context,address);
            Double latitude = latLng.latitude;
            Double longitude = latLng.longitude;
            // convert latLng due to here are two different LatLng in java
            // com.google.android.gms.maps.model.LatLng and com.google.maps.model.LatLng;
            PlacesSearchResult[] placesSearchResults = NearlyPlace.run(latitude, longitude).results;
            // reset the location icon size
            int height = 110;
            int width = 110;
            @SuppressLint("UseCompatLoadingForDrawables")
            BitmapDrawable bitMapDraw1 = (BitmapDrawable) getResources().getDrawable(R.drawable.gym1, null);
            @SuppressLint("UseCompatLoadingForDrawables")
            BitmapDrawable bitMapDraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.gym2, null);
            @SuppressLint("UseCompatLoadingForDrawables")
            BitmapDrawable bitMapDraw3 = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_user, null);
            Bitmap b1 = bitMapDraw1.getBitmap();
            Bitmap b2 = bitMapDraw2.getBitmap();
            Bitmap b3 = bitMapDraw3.getBitmap();
            Bitmap APIGymLogo = Bitmap.createScaledBitmap(b1, width, height, false); // blue icon
            Bitmap UserGymLogo = Bitmap.createScaledBitmap(b2, width, height, false); // red icon
            Bitmap UserLogo = Bitmap.createScaledBitmap(b3, width, height, false);

            // add nearly gym marker from google api
            for (PlacesSearchResult placesSearchResult : placesSearchResults) {
                double lat = placesSearchResult.geometry.location.lat;
                double lng = placesSearchResult.geometry.location.lng;
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(placesSearchResult.name)
                        .icon(BitmapDescriptorFactory.fromBitmap(APIGymLogo))
                );
            }

            // add marker of all Gym user's location on the map
            userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getRole().equals("Gym")) {
                        LatLng latLngGym = getLocationFromAddress(context, users.get(i).getAddress());
                        System.out.println(latLngGym);
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLngGym)
                                .title(users.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromBitmap(UserGymLogo))
                        );
                    }
                }
            });

            // set the default camera view and user's location
            if (!latLng.equals(new LatLng(-37.8136663, 144.9633777))) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                googleMap.setMinZoomPreference(6.0f);
                googleMap.setMaxZoomPreference(14.0f);
                Marker userLocation = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("You are here")
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(UserLogo))
                );
                assert userLocation != null;
                userLocation.showInfoWindow();
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                googleMap.setMinZoomPreference(6.0f);
                googleMap.setMaxZoomPreference(14.0f);
                Marker userLocation = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Position not find/default position")
                        .draggable(true)
                );
                assert userLocation != null;
                userLocation.showInfoWindow();
            }

            // zoom out button
            Button zoomOutButton = (Button) view.findViewById(R.id.zoom_out);
            zoomOutButton.setOnClickListener(view1 -> googleMap.animateCamera(CameraUpdateFactory.zoomOut()));

            // zoom in button
            Button zoomInButton = (Button) view.findViewById(R.id.zoom_in);
            zoomInButton.setOnClickListener(view13 -> googleMap.animateCamera(CameraUpdateFactory.zoomIn()));

            // zoom to current location icon button
            ImageButton locationButton = (ImageButton) view.findViewById(R.id.location);
            locationButton.setOnClickListener(view12 -> googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f)));
        });
        return view;
    }

    // get the lat and long from address
    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder geocoder = new Geocoder(context);
        //if the address is valid use this default address
        String defaultAddress = "350 Bourke Street Melbourne VIC 3000";
        List<Address> addressList;
        LatLng latLng = null;
        try {
            addressList = geocoder.getFromLocationName(strAddress, 1);
            if (addressList != null) {
                // This try catch can handle IndexOutOfBoundsException
                // Because if address is valid List<Address>.get(0)
                // will through IndexOutOfBoundsException
                try {
                    Address location = addressList.get(0);
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                } catch (IndexOutOfBoundsException ex1) {
                    addressList = geocoder.getFromLocationName(defaultAddress, 1);
                    Address location = addressList.get(0);
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        return latLng;
    }

}
