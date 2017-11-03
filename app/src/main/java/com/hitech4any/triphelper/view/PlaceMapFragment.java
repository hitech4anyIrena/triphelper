package com.hitech4any.triphelper.view;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.model.Place;

/**
 * Created by hitech4any on 16/09/2017.
 */

public class PlaceMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Place place;
    private MapView mapView;
    private View mView;

    public PlaceMapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_place_map, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (getContext() != null) MapsInitializer.initialize(getContext());
        if (place != null) {
            mMap = googleMap;
            LatLng placeLL = new LatLng(place.getLocationLat(), place.getLocationLon());
            //Log.e("place", place.getLocationLat() + "," + place.getLocationLon());
            mMap.addMarker(new MarkerOptions().position(placeLL));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLL, 14));
            mMap.addCircle(new CircleOptions().center(placeLL).radius(500).fillColor(Color.argb(158, 137, 247, 165)).strokeColor(Color.GREEN));
        }
    }
}
