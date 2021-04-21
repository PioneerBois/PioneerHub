package com.pandinu.PioneerHub.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pandinu.PioneerHub.R;


public class MapFragment extends Fragment {
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationRequest mLocationRequest;
    Location mCurrentLocation;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private final static String KEY_LOCATION = "location";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // when map is loaded
                LatLng CSUEBLocation = new LatLng(37.657278029434785, -122.05746618815536);
                float zoom = (float) 14;
                googleMap.setMinZoomPreference(8);
                googleMap.setMaxZoomPreference(18);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CSUEBLocation, zoom));
                Marker CSUEB = googleMap.addMarker(new MarkerOptions()
                        .position(CSUEBLocation)
                        .title("CSU East Bay")
                        .snippet("Our Campus")
                );
                CSUEB.showInfoWindow();

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions = new MarkerOptions();
                        // set position of marker
                        markerOptions.position(latLng);
                        // set title of marker
                        markerOptions.title(latLng.latitude + ":" + latLng.longitude);
                        // remove markers
                        googleMap.clear();
                        // animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });


            }
        });
        return view;
    }
}