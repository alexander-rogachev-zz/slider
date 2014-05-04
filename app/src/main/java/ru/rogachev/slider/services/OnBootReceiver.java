package ru.rogachev.slider.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import ru.rogachev.slider.utils.Constants;

public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            SharedPreferences sharedPref = context.getSharedPreferences(Constants.APP_PREFERENCES_NAME, Context.MODE_PRIVATE);
            boolean isAutoRunAfterRestart = sharedPref.getBoolean(Constants.CHECK_AUTO_RUN_AFTER_RESTART_PARAM_NAME, false);
            if (isAutoRunAfterRestart) {
                context.startService(new Intent(context, SliderService.class));
            }
            Log.v(this.getClass().getName(), "Service started while device boot completed.");
        }
    }
}
