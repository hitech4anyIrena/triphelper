package com.hitech4any.triphelper.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.controller.SearchIntentService;
import com.hitech4any.triphelper.interfaces.THAdapterListener;
import com.hitech4any.triphelper.model.Values;

/**
 * Created by hitech4any on 16/09/2017.
 */

public class AdvSearchFragment extends Fragment implements View.OnClickListener {
    private View searchV;
    private THAdapterListener listener;
    Context context;

    public AdvSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchV = inflater.inflate(R.layout.fragment_adv_search, container, false);
        searchV.findViewById(R.id.btn_nearby).setOnClickListener(this);
        listener = (THAdapterListener) getActivity();
        context = getContext();
        return searchV;
    }

    @Override
    public void onClick(View view) {
        double locationLat = listener.getLocationLat();
        double locationLon = listener.getLocationLon();

        if (!Values.checkInternetConnection(context, getActivity())||(locationLat == 0.0 && locationLon == 0.0)) {
            Toast.makeText(context, R.string.needProvider, Toast.LENGTH_SHORT).show();
            Log.e("onClickToast", locationLat+","+ locationLon);
            return;
        }
        Intent intent = new Intent(context, SearchIntentService.class);
        intent.putExtra(Values.LOCATION_LAT, locationLat);
        intent.putExtra(Values.LOCATION_LON, locationLon);
        intent.putExtra(Values.RADIUS, listener.getRadius());
        Log.e("onClick", locationLat+","+ locationLon);
        context.startService(intent);
    }
}
