package com.hitech4any.triphelper.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.controller.ListAdapter;
import com.hitech4any.triphelper.db.DBHelper;
import com.hitech4any.triphelper.model.Place;

import java.util.ArrayList;

/**
 * Created by hitech4any on 18/09/2017.
 */

public class FavoritesListFragment extends THListFragment {

    private ListAdapter adapter;
    private DBHelper helper;
    private RecyclerView favoriteRV;
    ArrayList<Place> favorites;

    public FavoritesListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        favoriteRV = (RecyclerView) inflater.inflate(R.layout.fragment_favorites_list, container, false);
        helper = new DBHelper(getActivity());
        adapter = new ListAdapter(getActivity());
        favoriteRV.setAdapter(adapter);
        favoriteRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        favorites = (ArrayList<Place>) helper.getAllFavorites();
        adapter.clear();
        adapter.addAll(favorites);
        return favoriteRV;

    }

    @Override
    public void onStart() {
        super.onStart();
        refreshList();
    }
    public void refreshList(){
        adapter.clear();
        favorites = (ArrayList<Place>) helper.getAllFavorites();
        //Log.e("places",places.toString());
        adapter.addAll(favorites);
    }
    public void remove(int position){
        Place place=adapter.getItem(position);
        adapter.remove(place);
        helper.deleteFavorite(place.getId());

    }
    public ListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void addFv(int position) {
    }
}
