package ru.rogachev.slider.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
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
        SharedPreferences sharedPref = getSharedPreferences(Constants.APP_PREFERENCES_NAME, MODE_PRIVATE);
        String startDatePref = sharedPref.getString(Constants.START_DATE_PARAM_NAME, null);
        String startTimePref = sharedPref.getString(Constants.START_TIME_PARAM_NAME, null);
        Date startDateTime = getDateTime(startDatePref, startTimePref);
        String endDatePref = sharedPref.getString(Constants.END_DATE_PARAM_NAME, null);
        String endTimePref = sharedPref.getString(Constants.END_TIME_PARAM_NAME, null);

        Date endDateTime = getDateTime(endDatePref, endTimePref);
        Date currentDate = new Date();

        if (isNotServiceRunning()) {
            boolean autoRunWhileChargingPref = sharedPref.getBoolean(Constants.CHECK_AUTO_RUN_PARAM_NAME, false);
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = registerReceiver(null, filter);
            int chargeState = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = chargeState == 2 || chargeState == 5;
            if (!isCharging || autoRunWhileChargingPref) {
                if (currentDate.after(startDateTime) && currentDate.before(endDateTime)) {
                    startActivity(slideShowIntent);
                }
            }
        } else {
            if (currentDate.after(endDateTime)) {
                sendBroadcast(new Intent(Constants.CLOSE_SLIDER_INTENT_NAME));
            }
        }
        return START_STICKY;
    }

    public boolean isNotServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo service : services) {
            if (service.topActivity.getClassName().equals(SlideShowActivity.class.getName())) {
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
