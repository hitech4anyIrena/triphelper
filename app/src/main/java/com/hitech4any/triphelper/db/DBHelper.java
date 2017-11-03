package com.hitech4any.triphelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hitech4any.triphelper.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitech4any on 21/09/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME_PLACE = "place";
    private static final String TABLE_NAME_FAVORITE = "favorite";

    private static final String COL_ID = "ID";
    private static final String COL_NAME = "name";
    private static final String COL_TYPE = "type";
    private static final String COL_LOC_LAT = "locationLat";
    private static final String COL_LOC_LON = "locationIng";
    private static final String COL_ADDRESS = "address";
    private static final String COL_ICON = "icon";
    private static final String COL_PRICE_LVL = "priceLvl";
    private static final String COL_RATING = "rating";
    private static final String COL_PHONE = "phone";
    private static final String COL_WEB = "web";
    public static final String TRIP_HELPER_DB = "tripHelper.db";

    public DBHelper(Context context) {
        super(context, TRIP_HELPER_DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT, %s TEXT," +
                        " %s REAL, %s REAL, %s TEXT, %s TEXT, %s INTEGER, %s REAL, %s TEXT, %s TEXT)",
                TABLE_NAME_PLACE, COL_ID, COL_NAME, COL_TYPE,
                COL_LOC_LAT, COL_LOC_LON, COL_ADDRESS, COL_ICON, COL_PRICE_LVL, COL_RATING, COL_PHONE, COL_WEB));
        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT, %s TEXT," +
                        " %s REAL, %s REAL, %s TEXT, %s TEXT, %s INTEGER, %s REAL, %s TEXT, %s TEXT)",
                TABLE_NAME_FAVORITE, COL_ID, COL_NAME, COL_TYPE,
                COL_LOC_LAT, COL_LOC_LON, COL_ADDRESS, COL_ICON, COL_PRICE_LVL, COL_RATING, COL_PHONE, COL_WEB));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // This method add place to placeDB
    public void insertPlace(Place place) throws SQLException {
        insertPlaceForTable(place, TABLE_NAME_PLACE);
    }
    // This method is replace places to placeDB
    public void replaceListPlaces(List<Place> places) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_PLACE, null, null);;
        for (Place place:places) {
            db.insertOrThrow(TABLE_NAME_PLACE, null, getPlaceValues(place));
        }
        db.close();
    }

    // This method add place to favoriteDB
    public void insertFavorite(Place place) throws SQLException {
        insertPlaceForTable(place, TABLE_NAME_FAVORITE);
    }

    private void insertPlaceForTable(Place place, String table) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        db.insertOrThrow(table, null, getPlaceValues(place));
        db.close();
    }

    // This method update place in the placeDB
    public void updatePlace(Place place) {
        updatePlaceForTable(place, TABLE_NAME_PLACE);
    }

    // This method update place in the favoriteDB
    public void updateFavorite(Place place) {
        updatePlaceForTable(place, TABLE_NAME_FAVORITE);
    }

    private void updatePlaceForTable(Place place, String table) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(table, getPlaceValues(place), COL_ID + "='" + place.getId()+"'", null);
        db.close();
    }

    //This method deletes the place which has this id from placeDB
    public void deletePlace(String id) {
        deletePlace(id, TABLE_NAME_PLACE);
    }

    //This method deletes the place which has this id from favoriteDB
    public void deleteFavorite(String id) {
        deletePlace(id, TABLE_NAME_FAVORITE);
    }

    private void deletePlace(String id, String table) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table, COL_ID + "='" + id+"'", null);
        db.close();
    }

    //This method deletes all places from placeDB
    public void deleteAllPlaces() {
        deleteAllPlacesFromTable(TABLE_NAME_PLACE);
    }

    //This method deletes all places from favoriteDB
    public void deleteAllFavorites() {
        deleteAllPlacesFromTable(TABLE_NAME_FAVORITE);
    }

    private void deleteAllPlacesFromTable(String table) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table, null, null);
        db.close();
    }

    //This method returns list of places
    public List<Place> getAllPlaces() {
        return getAllPlacesForTable(TABLE_NAME_PLACE);
    }

    //This method returns list of favorites
    public List<Place> getAllFavorites() {
        return getAllPlacesForTable(TABLE_NAME_FAVORITE);
    }
    public List<Place> getFavoritesForType(String type) {
        return getAllPlacesForTable(TABLE_NAME_FAVORITE, COL_TYPE + "='" + type+"'");
    }
    private List<Place> getAllPlacesForTable(String table) {
        return getAllPlacesForTable(table, "");
    }

    private List<Place> getAllPlacesForTable(String table, String where) {

        List<Place> places = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorPlaces = db.query(table, null, where, null, null, null, null);
        while (cursorPlaces.moveToNext()) {
            places.add(getPlace(db, cursorPlaces));
        }
        db.close();
        return places;
    }

    private ContentValues getPlaceValues(Place place) {
        //Log.e("start","");
        ContentValues values = new ContentValues();
        values.put(COL_ID, place.getId());
        values.put(COL_NAME, place.getName());
        values.put(COL_TYPE, place.getType());
        values.put(COL_LOC_LAT, place.getLocationLat());
        values.put(COL_LOC_LON, place.getLocationLon());
        values.put(COL_ADDRESS, place.getAddress());
        values.put(COL_ICON, place.getIcon());
        values.put(COL_PRICE_LVL, place.getPriceLevel());
        values.put(COL_RATING, place.getRating());
        values.put(COL_PHONE, place.getPhoneNum());
        values.put(COL_WEB, place.getWebSite());
        //Log.e("end",values+"");
        return values;
    }

    private Place getPlace(SQLiteDatabase db, Cursor cursorPlaces) {

        String id = cursorPlaces.getString(cursorPlaces.getColumnIndex(COL_ID));
        String name = cursorPlaces.getString(cursorPlaces.getColumnIndex(COL_NAME));
        String type = cursorPlaces.getString(cursorPlaces.getColumnIndex(COL_TYPE));
        double locationLat = cursorPlaces.getDouble(cursorPlaces.getColumnIndex(COL_LOC_LAT));
        double locationIng = cursorPlaces.getDouble(cursorPlaces.getColumnIndex(COL_LOC_LON));
        String address = cursorPlaces.getString(cursorPlaces.getColumnIndex(COL_ADDRESS));
        String icon = cursorPlaces.getString(cursorPlaces.getColumnIndex(COL_ICON));
        int priceLevel = cursorPlaces.getInt(cursorPlaces.getColumnIndex(COL_PRICE_LVL));
        double rating = cursorPlaces.getDouble(cursorPlaces.getColumnIndex(COL_RATING));
        String phoneNum = cursorPlaces.getString(cursorPlaces.getColumnIndex(COL_PHONE));
        String webSite = cursorPlaces.getString(cursorPlaces.getColumnIndex(COL_WEB));

        return new Place(id, name, type, locationLat, locationIng, address, icon, priceLevel, rating, phoneNum, webSite);
    }

}
