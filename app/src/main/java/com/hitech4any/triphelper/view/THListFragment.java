package com.hitech4any.triphelper.view;

import android.support.v4.app.Fragment;

import com.hitech4any.triphelper.controller.ListAdapter;

/**
 * Created by my-pc on 14/10/2017.
 */

public abstract class THListFragment extends Fragment{

    public abstract void refreshList();

    public abstract void remove(int position);

    public abstract ListAdapter getAdapter();

    public abstract void addFv(int position);
}
