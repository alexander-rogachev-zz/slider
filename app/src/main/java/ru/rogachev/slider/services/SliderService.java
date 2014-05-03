package ru.rogachev.slider.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.rogachev.slider.app.SlideShowActivity;
import ru.rogachev.slider.utils.Constants;


public class SliderService extends Service {

    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

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
        SharedPreferences sharedPref = getSharedPreferences("ru.rogachev.slider.app_preferences", MODE_PRIVATE);
        String startDatePref = sharedPref.getString(Constants.START_DATE_PARAM_NAME, null);
        String startTimePref = sharedPref.getString(Constants.START_TIME_PARAM_NAME, null);
        Date startDateTime = getDateTime(startDatePref, startTimePref);
        String endDatePref = sharedPref.getString(Constants.END_DATE_PARAM_NAME, null);
        String endTimePref = sharedPref.getString(Constants.END_TIME_PARAM_NAME, null);
        Date endDateTime = getDateTime(endDatePref, endTimePref);
        Date currentDate = new Date();
        if (isNotServiceRunning()) {
            if (currentDate.after(startDateTime) && currentDate.before(endDateTime)) {
                startActivity(slideShowIntent);
            }
        } else {
            if (currentDate.after(endDateTime)) {
                sendBroadcast(new Intent("xyz"));
            }
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

    public Date getDateTime(String dateString, String timeString) {
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String[] timeItems = timeString.split(":");
        calendar.set(Calendar.HOUR, Integer.parseInt(timeItems[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeItems[1]));
        return calendar.getTime();
    }
}
