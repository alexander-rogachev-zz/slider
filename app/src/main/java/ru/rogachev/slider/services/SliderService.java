package ru.rogachev.slider.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import ru.rogachev.slider.app.SlideShowActivity;


public class SliderService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Intent slideShowIntent = new Intent(this, SlideShowActivity.class);
        slideShowIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isNotServiceRunning()) {
           // startActivity(slideShowIntent);
        }
        return START_STICKY;
    }

    public boolean isNotServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo service : services) {
            if (service.topActivity.getClassName().equals("ru.rogachev.slider.app.SlideShowActivity")) {
                return false;
            }
        }
        return true;
    }
}
