package ru.rogachev.slider.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import edu.android.openfiledialog.OpenFileDialog;
import ru.rogachev.slider.dialogs.TimePickerDialogWithSeconds;
import ru.rogachev.slider.utils.Constants;

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        final SharedPreferences sharedPref = getPreferenceManager() != null ? getPreferenceManager().getSharedPreferences() : null;
        if (sharedPref == null) {
            return;
        }

        final Preference slideDelayPref = findPreference(Constants.DELAY_PARAM_NAME);
        if (slideDelayPref != null) {
            String delay = sharedPref.getString(Constants.DELAY_PARAM_NAME, null);
            if (delay != null) {
                slideDelayPref.setSummary(delay);
            }
            slideDelayPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Calendar calendar = Calendar.getInstance();
                    TimePickerDialogWithSeconds dialog = new TimePickerDialogWithSeconds(getActivity(), new TimePickerDialogWithSeconds.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(ru.rogachev.slider.dialogs.TimePicker view, int hourOfDay, int minute, int second) {
                            String hours = String.format("%02d", hourOfDay);
                            String minutes = String.format("%02d", minute);
                            String seconds = String.format("%02d", second);
                            slideDelayPref.setSummary(hours + ":" + minutes + ":" + seconds);
                            SharedPreferences sharedPref = getPreferenceManager().getSharedPreferences();
                            SharedPreferences.Editor prefEditor = sharedPref.edit();
                            prefEditor.putString(Constants.START_TIME_PARAM_NAME, hours + ":" + minutes + ":" + seconds);
                            prefEditor.commit();
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), true);
                    dialog.show();
                    return true;
                }
            });
        }

        final Preference dateStartPref = findPreference(Constants.START_DATE_PARAM_NAME);
        if (dateStartPref != null) {
            String sDate = sharedPref.getString(Constants.START_DATE_PARAM_NAME, null);

            if (sDate != null) {
                dateStartPref.setSummary(sDate);
            }
            dateStartPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Calendar calendar = Calendar.getInstance();
                    Dialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String date = String.format("%04d", year) + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
                            dateStartPref.setSummary(date);
                            SharedPreferences.Editor prefEditor = sharedPref.edit();
                            prefEditor.putString(Constants.START_DATE_PARAM_NAME, date);
                            prefEditor.commit();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                    return true;
                }
            });
        }

        final Preference timeStartPref = findPreference(Constants.START_TIME_PARAM_NAME);
        if (timeStartPref != null) {
            String sTime = sharedPref.getString(Constants.START_TIME_PARAM_NAME, null);
            if (sTime != null) {
                timeStartPref.setSummary(sTime);
            }
            timeStartPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Calendar calendar = Calendar.getInstance();
                    Dialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hours = String.format("%02d", hourOfDay);
                            String minutes = String.format("%02d", minute);
                            timeStartPref.setSummary(hours + ":" + minutes);
                            SharedPreferences sharedPref = getPreferenceManager().getSharedPreferences();
                            SharedPreferences.Editor prefEditor = sharedPref.edit();
                            prefEditor.putString(Constants.START_TIME_PARAM_NAME, hours + ":" + minutes);
                            prefEditor.commit();
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    dialog.show();
                    return true;
                }
            });
        }

        final Preference dateEndPref = findPreference(Constants.END_DATE_PARAM_NAME);
        if (dateEndPref != null) {
            String eDate = sharedPref.getString(Constants.END_DATE_PARAM_NAME, null);
            if (eDate != null) {
                dateEndPref.setSummary(eDate);
            }
            dateEndPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Calendar calendar = Calendar.getInstance();
                    Dialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String date = String.format("%04d", year) + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
                            dateEndPref.setSummary(date);
                            SharedPreferences sharedPref = getPreferenceManager().getSharedPreferences();
                            SharedPreferences.Editor prefEditor = sharedPref.edit();
                            prefEditor.putString(Constants.END_DATE_PARAM_NAME, date);
                            prefEditor.commit();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                    return true;
                }
            });
        }

        final Preference timeEndPref = findPreference(Constants.END_TIME_PARAM_NAME);
        if (timeEndPref != null) {
            String eTime = sharedPref.getString(Constants.END_TIME_PARAM_NAME, null);
            if (eTime != null) {
                timeEndPref.setSummary(eTime);
            }
            timeEndPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Calendar calendar = Calendar.getInstance();
                    Dialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hours = String.format("%02d", hourOfDay);
                            String minutes = String.format("%02d", minute);
                            timeEndPref.setSummary(hours + ":" + minutes);
                            SharedPreferences sharedPref = getPreferenceManager().getSharedPreferences();
                            SharedPreferences.Editor prefEditor = sharedPref.edit();
                            prefEditor.putString(Constants.END_TIME_PARAM_NAME, hours + ":" + minutes);
                            prefEditor.commit();
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    dialog.show();
                    return true;
                }
            });
        }

        final Preference folderNamePref = findPreference(Constants.FOLDER_PARAM_NAME);
        if (folderNamePref != null) {
            String fName = sharedPref.getString(Constants.FOLDER_PARAM_NAME, null);
            if (fName != null) {
                folderNamePref.setSummary(fName);
            }
            folderNamePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    OpenFileDialog dialog = new OpenFileDialog(getActivity())
                            .setFilter(".*\\.*")
                            .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                                @Override
                                public void OnSelectedFile(String fileName) {
                                    folderNamePref.setSummary(fileName);
                                    SharedPreferences sharedPref = getPreferenceManager().getSharedPreferences();
                                    SharedPreferences.Editor prefEditor = sharedPref.edit();
                                    prefEditor.putString(Constants.FOLDER_PARAM_NAME, fileName);
                                    prefEditor.commit();
                                }
                            });
                    dialog.show();
                    return true;
                }
            });
        }
    }
}
