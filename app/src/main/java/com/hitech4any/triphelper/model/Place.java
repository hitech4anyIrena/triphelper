package com.hitech4any.triphelper.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by hitech4any on 16/09/2017.
 */

public class Place implements Parcelable {
    private String id;
    private String name;
    private String type;
    private double locationLat;
    private double locationLon;
    private String address;
    private String icon;
    private int priceLevel;
    private double rating;
    private String phoneNum;
    private String webSite;

    public Place(String id, String name, String type, double locationLat, double locationLon, String address, String icon, int priceLevel, double rating, String phoneNum, String webSite) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.address = address;
        this.icon = icon;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.phoneNum = phoneNum;
        this.webSite = webSite;
    }

    protected Place(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        locationLat = in.readDouble();
        locationLon = in.readDouble();
        address = in.readString();
        icon = in.readString();
        priceLevel = in.readInt();
        rating = in.readDouble();
        phoneNum = in.readString();
        webSite = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public double getLocationLon() {
        return locationLon;
    }

    public String getAddress() {
        return address;
    }

    public String getIcon() {
        return icon;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public double getRating() {
        return rating;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public void setLocationLon(double locationLon) {
        this.locationLon = locationLon;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeDouble(locationLat);
        parcel.writeDouble(locationLon);
        parcel.writeString(address);
        parcel.writeString(icon);
        parcel.writeInt(priceLevel);
        parcel.writeDouble(rating);
        parcel.writeString(phoneNum);
        parcel.writeString(webSite);
    }

    public String getStringDistance(double locationLat, double locationLon, boolean km) {
        if (locationLat == 0.0 && locationLon == 0.0) return "";
        double distance = distance(locationLat, locationLon, this.locationLat, this.locationLon, km);
        if (!km) {
            return distance + " miles";
        }
        return (int) (distance * Values.KM_TO_METERS) + "meters";
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, boolean km) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (km) {
            dist = dist * 1.609344;
        }
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
