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
 * Created by hitech4any on 16/09/2017.
 */

public class MainFragment extends FrameFragment {

 public MainFragment() {}

    @Override
    protected THListFragment setTHListFrag() {
        return new PlaceListFragment();
    }

}
