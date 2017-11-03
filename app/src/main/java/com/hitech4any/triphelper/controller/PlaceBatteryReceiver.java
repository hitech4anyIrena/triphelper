package com.hitech4any.triphelper.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hitech4any.triphelper.R;

/**
 * Created by hitech4any on 21/09/2017.
 */


public class PlaceBatteryReceiver extends BroadcastReceiver {

    private OnBatteryChangedListener listener;

    public PlaceBatteryReceiver(OnBatteryChangedListener listener) {

        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int message;
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
            message = R.string.usb;
        }
        else {
            message =  R.string.unplugged;
        }

        listener.batteryChanged(message);
    }

    public interface OnBatteryChangedListener {
        void batteryChanged(int info);
    }

}
