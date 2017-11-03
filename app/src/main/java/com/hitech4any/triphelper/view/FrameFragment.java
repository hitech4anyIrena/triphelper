package com.hitech4any.triphelper.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.model.Values;

/**
 * Created by hitech4any on 14/10/2017.
 */

public abstract class FrameFragment extends Fragment {

    private THListFragment THListFrag;
    private PlaceMapFragment PMFrag;
    private boolean flagFrags = false;
    View rootVew;

    public FrameFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PMFrag = new PlaceMapFragment();
        THListFrag = setTHListFrag();
        setRetainInstance(true);
    }

    protected abstract THListFragment setTHListFrag();

    public boolean isFlagFrags() {
        return flagFrags;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootVew=inflater.inflate(R.layout.fragment_main, container, false);
        flagFrags = false;
        if (rootVew.findViewById(R.id.fragMapContainer) != null) {
            if(!PMFrag.isAdded()) {
                flagFrags = true;
                getChildFragmentManager().beginTransaction()
                        .add(R.id.fragMapContainer, PMFrag)
                        .commit();
            }
        }
        if(!THListFrag.isAdded()) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragListContainer, THListFrag)
                    .commit();
        }
        //Log.e("onCreateView", "flagFrags ="+ flagFrags+", PLFrag ="+PLFrag+",PMFrag ="+PMFrag);
        return rootVew;

    }
    public void onClick(int position,Context pack) {
        if (flagFrags) {
            PMFrag = new PlaceMapFragment();
            PMFrag.setPlace(THListFrag.getAdapter().getItem(position));
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragMapContainer, PMFrag)
                    .commit();

        } else {
            Intent intent = new Intent(getActivity(), MapPlaceActivity.class);
            ////Log.e("onClick",PLFrag.getAdapter().getItem(position).toString());

            intent.putExtra(Values.PLACE, THListFrag.getAdapter().getItem(position));
            startActivity(intent);
        }
    }

    public void remove(int position) {
        THListFrag.remove(position);
    }

    public void refreshList() {
        THListFrag.refreshList();
    }


    public void addFv(int position) {
        THListFrag.addFv(position);

    }

}
