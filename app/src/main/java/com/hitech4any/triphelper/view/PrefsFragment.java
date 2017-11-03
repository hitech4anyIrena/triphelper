package com.hitech4any.triphelper.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.hitech4any.triphelper.R;

/**
 * Created by hitech4any on 16/09/2017.
 */

public class PrefsFragment extends PreferenceFragment{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}