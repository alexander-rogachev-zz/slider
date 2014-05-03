package ru.rogachev.slider.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rogachev on 03.05.2014.
 */
public class PowerReceiver  extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Log.d("DEBUG", "Power connected...");
        }
    }
}