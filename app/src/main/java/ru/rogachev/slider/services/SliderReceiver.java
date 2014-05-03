package ru.rogachev.slider.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.rogachev.slider.app.SlideShowActivity;

public class SliderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SlideShowActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
