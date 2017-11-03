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
 * Created by my-pc on 14/10/2017.
 */

public class FavoriteFragment extends FrameFragment {

    @Override
    protected THListFragment setTHListFrag() {
        return new FavoritesListFragment();
    }
}
