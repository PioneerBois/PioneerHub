package com.pandinu.PioneerHub.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.pandinu.PioneerHub.Map;
import com.pandinu.PioneerHub.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;


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
        //setRetainInstance(true);
    }

    public static MapFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
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
                final LatLng CSUEBLocation = new LatLng(37.657278029434785, -122.05746618815536);
                float zoom = (float) 16;
                googleMap.setMinZoomPreference(8);
                googleMap.setMaxZoomPreference(20);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CSUEBLocation, zoom));


                ParseQuery<Map> query = ParseQuery.getQuery(Map.class);
                query.findInBackground(new FindCallback<Map>() {
                    @Override
                    public void done(List<Map> mapLocations, ParseException e) {
                        Log.i("Number of Locations", String.valueOf(mapLocations.size()));
                        for(int i = 0; i < mapLocations.size(); i++){
                            Map location = mapLocations.get(i);
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng((double) location.getLatitude(), (double) location.getLongitude()))
                                    .title(location.getLocationName())

                            );

                        }
                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                // marker.getTitle();
                                // marker.getPosition();
                                return false;
                            }
                        });

                    }
                });


            }
        });
        return view;
    }
}