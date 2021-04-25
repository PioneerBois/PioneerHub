package com.pandinu.PioneerHub;

import com.parse.ParseClassName;
import com.parse.ParseObject;
@ParseClassName("MapLocation")
public class Map extends ParseObject {
    public static final String LOCATION_NAME = "LocationName";
    public static final String LAT = "Latitude";
    public static final String LONG = "Longitude";
    public static final String DESC = "Description";

    public String getLocationName() { return getString(LOCATION_NAME);}
    public String getDescription(){ return getString(DESC);}
    public Number getLongitude() { return getNumber(LONG);}
    public Number getLatitude (){ return getNumber(LAT);}
}
