package io.evolution.downtohang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by michael on 4/9/2016.
 */

public class preferencesActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


        final CheckBoxPreference notificationPref = (CheckBoxPreference) getPreferenceManager().findPreference("notificationPreference");

        final CheckBoxPreference gpsPref = (CheckBoxPreference) getPreferenceManager().findPreference("gpsPreference");

        notificationPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("MyApp", "Pref " + preference.getKey() + " changed to " + newValue.toString());
                return true;
            }
        });

        gpsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("MyApp", "Pref " + preference.getKey() + " changed to " + newValue.toString());
                return true;
            }
        });

    }

    public void onStart(Intent intent, int startId) {
        getPrefs();
    }

    String ListPreference;
    boolean CheckboxPreference;

    private void getPrefs() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        ListPreference = prefs.getString("listPref", "nr1");
        CheckboxPreference = prefs.getBoolean("checkboxPref", true);
    }



}