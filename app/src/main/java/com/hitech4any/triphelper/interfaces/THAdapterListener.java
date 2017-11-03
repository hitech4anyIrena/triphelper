package com.hitech4any.triphelper.interfaces;

/**
 * Created by my-pc on 14/10/2017.
 */

public interface THAdapterListener {
    double getLocationLat();

    double getLocationLon();

    String getMeasure();

    void onClick(int position);

    void onInfClick(int position);

    void onDeleteClick(int position);

    void onFavoriteClick(int position);

    void onRefresh();

    double getRadius();
}
