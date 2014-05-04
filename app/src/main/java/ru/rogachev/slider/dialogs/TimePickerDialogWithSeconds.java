package ru.rogachev.slider.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import ru.rogachev.slider.app.R;

public class TimePickerDialogWithSeconds extends AlertDialog implements DialogInterface.OnClickListener {

    public interface OnTimeSetListener {
        void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";

    private final TimePicker mTimePicker;
    private final OnTimeSetListener mCallback;
    private boolean visible = false;

    int mInitialHourOfDay;
    int mInitialMinute;
    int mInitialSeconds;

    public TimePickerDialogWithSeconds(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, int seconds) {
        this(context, 0, callBack, hourOfDay, minute, seconds);
    }

    public TimePickerDialogWithSeconds(Context context, int theme, OnTimeSetListener callBack, int hourOfDay, int minute,
                                       int seconds) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mInitialSeconds = seconds;

        setButton(DialogInterface.BUTTON_POSITIVE, context.getText(R.string.time_set), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getText(R.string.cancel), (OnClickListener) null);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!visible) {
                    dismiss();
                }
            }
        });

        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setCurrentSecond(mInitialSeconds);
        setTitle(context.getText(R.string.time_picker_widget_title));
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(), mTimePicker.getCurrentSeconds());
        }
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putInt(SECONDS, mTimePicker.getCurrentSeconds());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setCurrentSecond(seconds);
    }

}
