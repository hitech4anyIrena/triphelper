package com.hitech4any.triphelper.controller;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.db.DBHelper;
import com.hitech4any.triphelper.model.Place;
import com.hitech4any.triphelper.model.Values;
import com.hitech4any.triphelper.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitech4any on 21/09/2017.
 */

public class SearchIntentService extends IntentService {

    public static final String SEARCH_PLACE_ACTION = Values.PACKAGE + Values.SEARCH_SERVICE;
    HttpURLConnection connection;
    BufferedReader reader;
    StringBuilder builder;
    String urlString;
    URL url;

    public SearchIntentService() {
        super(Values.SEARCH_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Place> results = new ArrayList<>();
        String keyword = intent.getStringExtra(Values.KEYWORD);
        String type = intent.getStringExtra(Values.TYPE);
        String location = intent.getDoubleExtra(Values.LOCATION_LAT, 0.0) + "," + intent.getDoubleExtra(Values.LOCATION_LON, 0.0);
        String radius = intent.getIntExtra(Values.RADIUS, Values.getRadius(1, true)) + "";

        urlString = Values.SEARCH_URL_NEARBY + Values.LOCATION_S + location + Values.RADIUS_S + radius;
        if ((type != null) && (!type.isEmpty())) {
            urlString += Values.TYPE_S + type;
        }
        if ((keyword != null) && (!keyword.isEmpty())) {
            urlString += Values.KEYWORD_S + keyword;
        }
        urlString += Values.SEARCH_KEY;
        connection = null;
        //---------------
        Log.e("url", urlString);
        //--------------
        builder = new StringBuilder();
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return;
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }

            JSONObject root = new JSONObject(builder.toString());
            if (!root.getString(Values.STATUS_S).equalsIgnoreCase(Values.STATUS_S_OK)) {
                if (connection != null) {
                    connection.disconnect();
                }
                return;
            }
            results = addResults(results, root.getJSONArray(Values.RESULTS_S));
            String token = "";
            try {
                token = root.getString(Values.NPTOKEN_S);
            } catch (JSONException e) {
                token = "";
                e.printStackTrace();
            }
            while (token.length() > 0) {
                urlString = Values.SEARCH_URL_NEARBY + Values.PTOKEN_S + token + Values.SEARCH_KEY;
                //---------------
                Log.e("url1", urlString);
                //--------------
                builder = new StringBuilder();
                url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    token = "";
                    return;
                }

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                root = new JSONObject(builder.toString());
                results = addResults(results, root.getJSONArray(Values.RESULTS_S));
                try {
                    token = root.getString(Values.NPTOKEN_S);
                } catch (JSONException e) {
                    token = "";
                    e.printStackTrace();
                }
                if (!root.getString(Values.STATUS_S).equalsIgnoreCase(Values.STATUS_S_OK)) {
                    token = "";
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        if (!results.isEmpty()) {
            new DBHelper(this).replaceListPlaces(results);
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SEARCH_PLACE_ACTION));
            sendNotif();
        }
    }

    private List<Place> addResults(List<Place> results, JSONArray rootArray) {
        for (int i = 0; i < rootArray.length(); i++) {
            //for each object used own try / catch to continue after exception
            try {
                JSONObject placeObj = rootArray.getJSONObject(i);
                String id = placeObj.getString(Values.ID_R);
                String name = placeObj.getString(Values.NAME_R);
                String type = "";
                double locationLat = placeObj.getJSONObject(Values.GEOMETRY_R).getJSONObject(Values.LOCATION_R).getDouble(Values.LAT_R);
                double locationLon = placeObj.getJSONObject(Values.GEOMETRY_R).getJSONObject(Values.LOCATION_R).getDouble(Values.LON_R);
                try {
                    type = placeObj.getJSONArray(Values.TYPES_R).getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //for each nullable value used own try / catch to continue after exception
                String address = "";
                String icon = "";
                int priceLevel = 0;
                double rating = 0.0;
                String phoneNum = "";
                String webSite = "";
                try {
                    icon = placeObj.getString(Values.ICON_R).replaceAll(".+/|-|\\..+", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    priceLevel = placeObj.getInt(Values.PRICE_LEVEL_R);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rating = placeObj.getDouble(Values.RATING_R);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                urlString = Values.SEARCH_URL_DETAILS + id + Values.SEARCH_KEY;
                //---------------
                //Log.e("url2", urlString);
                //--------------
                try {
                    builder = new StringBuilder();
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    {
                        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) break;
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line = reader.readLine();
                        while (line != null) {
                            builder.append(line);
                            line = reader.readLine();
                        }
                        JSONObject root = new JSONObject(builder.toString());
                        if (!root.getString(Values.STATUS_S).equalsIgnoreCase(Values.STATUS_S_OK))
                            break;
                        JSONObject resultObj = root.getJSONObject(Values.RESULT_S);
                        address = resultObj.getString(Values.FORMATTED_ADDRESS_R);
                        phoneNum = resultObj.getString(Values.INTERNATIONAL_PHONE_NUMBER_R);
                        webSite = resultObj.getString(Values.WEBSITE_R);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

                results.add(new Place(id, name, type, locationLat, locationLon, address, icon, priceLevel, rating, phoneNum, webSite));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    //notification of success search
    private void sendNotif() {

        Intent newIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationCompat = new NotificationCompat.Builder(this)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(this.getString(R.string.endSearch))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat.from(this).notify(1, notificationCompat);


    }


}
