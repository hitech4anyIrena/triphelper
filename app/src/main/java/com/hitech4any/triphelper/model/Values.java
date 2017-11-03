package com.hitech4any.triphelper.model;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by hitech4any on 21/09/2017.
 * //AIzaSyDqLbtC0a3wnUMSzdzJMzw2jmcrP58JaA4 - API Maps
 */

public final class Values {
    //defaults
    public static final String DIF_PERIODICITY = "200";
    public static final String DIF_RADIUS = "1.0";
    public static final String DIF_MEASURE = "kilometers";
    //intent values
    public static final String LOCATION_LAT = "locationLat";
    public static final String LOCATION_LON = "locationLon";
    public static final String GOT_LOCATION = "gotLocation";
    public static final String KEYWORD = "keyword";
    public static final String RADIUS= "radius";
    public static final String TYPE= "type";
    public static final String PLACE= "place";
    public static final String PROVIDER= "provider";

    //constants
    public static final int MILES_R = 3959;
    public static final int KM_R = 6371;
    public static final double METER_TO_MILE=0.000621371;
    public static final double MILES_TO_METERS = 1609.34;
    public static final int KM_TO_METERS = 1000;
    public static final int LOCATION_UPD_RADIUS = 100;
    public static final int TIMER = 10000;
    //search values
    public static final String PACKAGE = "com.hitech4any.triphelper.";
    public static final String SEARCH_SERVICE = "SearchIntentService";
    public static final String SEARCH_KEY= "&key=AIzaSyDgZ1tokKNRE1Z6Kvze5c8X9M6G16SyEBo";
    public static final String SEARCH_URL_NEARBY= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static final String SEARCH_URL_DETAILS= "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    public static final String LOCATION_S= "location=";
    public static final String RADIUS_S= "&radius=";
    public static final String TYPE_S= "&type=";
    public static final String KEYWORD_S= "&keyword=";
    public static final String STATUS_S= "status";
    public static final String STATUS_S_OK= "ok";
    public static final String RESULTS_S= "results";
    public static final String RESULT_S= "result";
    public static final String NPTOKEN_S= "next_page_token";
    public static final String PTOKEN_S= "pagetoken=";
    public static final String NAME_R= "name";
    public static final String ID_R = "place_id";
    public static final String GEOMETRY_R = "geometry";
    public static final String LOCATION_R = "location";
    public static final String LAT_R = "lat";
    public static final String LON_R = "lng";
    public static final String TYPES_R = "types";
    public static final String ICON_R = "icon";
    public static final String PRICE_LEVEL_R = "price_level";
    public static final String RATING_R = "rating";
    public static final String FORMATTED_ADDRESS_R = "formatted_address";
    public static final String INTERNATIONAL_PHONE_NUMBER_R = "international_phone_number";
    public static final String WEBSITE_R = "website";
    public static int MAX_RADIUS=50000; //Google API


    public static int getRadius(double radius, boolean isKm){
        int measureRadius=(int)(radius* MILES_TO_METERS);
        if (isKm){
            measureRadius=(int)radius* KM_TO_METERS;
        }
        if (measureRadius>MAX_RADIUS) measureRadius=MAX_RADIUS;
        return measureRadius;
    }

    public static boolean checkInternetConnection(Context context,Activity activity) {
        // check permission
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 11);
            //Log.e("permission","ACCESS_FINE_LOCATION "+android.Manifest.permission.ACCESS_FINE_LOCATION + " INTERNET" +android.Manifest.permission.INTERNET);
            return false;
        }
        try
        {
            //Log.e("ConnectivityManager","start");
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                //Log.e("ConnectivityManager", conMgr.getActiveNetworkInfo().isConnected() + "");
                return true;
            }else {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

}
