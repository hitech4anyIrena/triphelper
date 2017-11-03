package com.hitech4any.triphelper.view;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.controller.ListAdapter;
import com.hitech4any.triphelper.controller.SearchIntentService;
import com.hitech4any.triphelper.db.DBHelper;
import com.hitech4any.triphelper.model.Place;

import java.util.ArrayList;

/**
 * Created by hitech4any on 18/09/2017.
 */

public class PlaceListFragment extends THListFragment {

    private ListAdapter adapter;
    private DBHelper helper;
    private RecyclerView placeRV;
    ArrayList<Place> places;
    BroadcastReceiver receiver;
    IntentFilter filter;

    public PlaceListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        placeRV = (RecyclerView) inflater.inflate(R.layout.fragment_place_list, container, false);
        helper = new DBHelper(getActivity());
        adapter = new ListAdapter(getActivity());
        placeRV.setAdapter(adapter);
        adapter.clear();
        placeRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        receiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshList();
            }
        };

        filter = new IntentFilter(SearchIntentService.SEARCH_PLACE_ACTION);
        registerForContextMenu(placeRV);
        return placeRV;

    }

    @Override
    public void onStart() {
        super.onStart();
        refreshList();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }
    public void refreshList(){
        adapter.clear();
        places = (ArrayList<Place>) helper.getAllPlaces();
        //Log.e("places",places.toString());
        adapter.addAll(places);
    }

    public ListAdapter getAdapter() {
        return adapter;
    }

    public void remove(int position){

        Place place=adapter.getItem(position);
        adapter.remove(place);
        //Log.e("Frag",position+" place "+ place);
        helper.deletePlace(place.getId());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.shareMenu:

                return true;

            case R.id.addToFvMenu:
                helper.insertFavorite(adapter.getItem(info.position));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void addFv(int position) {
        try{
            helper.insertFavorite(adapter.getItem(position));
        }catch (SQLiteException e){
            e.printStackTrace();
        }

    }
}