package ru.rogachev.slider.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "kk:mm";
    private static final int DIALOG_START_DATE = 0;
    private static final int DIALOG_START_TIME = 1;
    private static final int DIALOG_END_DATE = 2;
    private static final int DIALOG_END_TIME = 3;
    public static final String START_DATE_PARAM_NAME = "startDate";
    public static final String START_TIME_PARAM_NAME = "startTime";
    public static final String END_DATE_PARAM_NAME = "endDate";
    public static final String END_TIME_PARAM_NAME = "endTime";
    private static final String CHECK_AUTO_RUN_PARAM_NAME = "autoRun";
    private static final String CHECK_AUTO_RUN_AFTER_RESTART_PARAM_NAME = "autoRunAfterRestart";

    private Button btnStartDate;
    private Button btnStartTime;
    private Button btnEndDate;
    private Button btnEndTime;
    private CheckBox cbAutoRun;
    private CheckBox cbAutoRunAfterRestart;
    private String date;
    private String time;
    private String hours;
    private String minutes;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("slider.settings", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        Date now = new Date();

        date = sharedPref.getString(START_DATE_PARAM_NAME, DateFormat.format(DATE_FORMAT, now).toString());
        btnStartDate = (Button) findViewById(R.id.btn_start_date);
        btnStartDate.setText(date);
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyTracker.getTracker().sendEvent("ui_action", "on_click", "button_date", null);

                showDialog(DIALOG_START_DATE);
            }
        });

        time = sharedPref.getString(START_TIME_PARAM_NAME, DateFormat.format(TIME_FORMAT, now).toString());
        btnStartTime = (Button) findViewById(R.id.btn_start_time);
        btnStartTime.setText(time);
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyTracker.getTracker().sendEvent("ui_action", "on_click", "button_time", null);

                showDialog(DIALOG_START_TIME);
            }
        });

        date = sharedPref.getString(END_DATE_PARAM_NAME, DateFormat.format(DATE_FORMAT, now).toString());
        btnEndDate = (Button) findViewById(R.id.btn_end_date);
        btnEndDate.setText(date);
        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyTracker.getTracker().sendEvent("ui_action", "on_click", "button_date", null);

                showDialog(DIALOG_END_DATE);
            }
        });

        time = sharedPref.getString(END_TIME_PARAM_NAME, DateFormat.format(TIME_FORMAT, now).toString());
        btnEndTime = (Button) findViewById(R.id.btn_end_time);
        btnEndTime.setText(time);
        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyTracker.getTracker().sendEvent("ui_action", "on_click", "button_time", null);

                showDialog(DIALOG_END_TIME);
            }
        });

        boolean isAutoRun = sharedPref.getBoolean(CHECK_AUTO_RUN_PARAM_NAME, false);
        cbAutoRun = (CheckBox) findViewById(R.id.cb_autorun);
        cbAutoRun.setChecked(isAutoRun);
        cbAutoRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putBoolean(CHECK_AUTO_RUN_PARAM_NAME, isChecked);
                prefEditor.commit();
            }
        });

        boolean isAutoRunAfterRestart = sharedPref.getBoolean(CHECK_AUTO_RUN_AFTER_RESTART_PARAM_NAME, false);
        cbAutoRunAfterRestart = (CheckBox) findViewById(R.id.cb_autorun_afterrestart);
        cbAutoRunAfterRestart.setChecked(isAutoRunAfterRestart);
        cbAutoRunAfterRestart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putBoolean(CHECK_AUTO_RUN_AFTER_RESTART_PARAM_NAME, isChecked);
                prefEditor.commit();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch (id) {
            case DIALOG_START_DATE: {
                Calendar calendar = Calendar.getInstance();
                dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = String.format("%04d", year) + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
                        btnStartDate.setText(date);
                        SharedPreferences.Editor prefEditor = sharedPref.edit();
                        prefEditor.putString(START_DATE_PARAM_NAME, date);
                        prefEditor.commit();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                break;
            }
            case DIALOG_START_TIME: {
                Calendar calendar = Calendar.getInstance();
                dialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hours = String.format("%02d", hourOfDay);
                        minutes = String.format("%02d", minute);
                        btnStartTime.setText(hours + ":" + minutes);
                        SharedPreferences.Editor prefEditor = sharedPref.edit();
                        prefEditor.putString(START_TIME_PARAM_NAME, hours + ":" + minutes);
                        prefEditor.commit();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                break;
            }
            case DIALOG_END_DATE: {
                Calendar calendar = Calendar.getInstance();

                dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = String.format("%04d", year) + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
                        btnEndDate.setText(date);
                        SharedPreferences.Editor prefEditor = sharedPref.edit();
                        prefEditor.putString(END_DATE_PARAM_NAME, date);
                        prefEditor.commit();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                break;
            }
            case DIALOG_END_TIME: {
                Calendar calendar = Calendar.getInstance();
                dialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hours = String.format("%02d", hourOfDay);
                        minutes = String.format("%02d", minute);
                        btnEndTime.setText(hours + ":" + minutes);
                        SharedPreferences.Editor prefEditor = sharedPref.edit();
                        prefEditor.putString(END_TIME_PARAM_NAME, hours + ":" + minutes);
                        prefEditor.commit();

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                break;
            }
            default:
                dialog = null;
        }

        return dialog;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(MainActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(MainActivity.this);
    }

}
