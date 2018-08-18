package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.prefs.PreferenceChangeListener;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Get minMagnitude preference by finding it by its key
            // Use helper method to display the preference's value in its summary
            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            // Find orderBy preference by its key
            // Use helper method to display the preference's value in its summary
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            // Find maxResults preference by its key
            // Use helper method to display the preference's value in its summary
            Preference maxResults = findPreference(getString(R.string.settings_max_results_key));
            bindPreferenceSummaryToValue(maxResults);

        }

        /**
         * When the preference is changed, update the preference summary with the new value
         * @param preference is the preference
         * @param newValue is the newly changed preference's value
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // Create string from the newValue, and set it to the preference's summary
            String valueString = newValue.toString();

            // Checks If the preference is a list preference
            if (preference instanceof ListPreference){
                // Cast preference as a list preference
                ListPreference listPreference = (ListPreference) preference;

                // get the index value of valueString
                int prefIndex = listPreference.findIndexOfValue(valueString);

                // and if its greater than or equal to 0
                if(prefIndex >= 0){
                    // create an array of the listPreferences entries
                    CharSequence[] labels = listPreference.getEntries();
                    // set summary to the currently selected preference entry
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                // set preference summary to valueString
                preference.setSummary(valueString);
            }
            return true;
        }

        /**
         * Display the value of the preference in its summary,
         * @param preference is the users settings page preference
         */
        private void bindPreferenceSummaryToValue(Preference preference){
            // Set an on preference change listener
            preference.setOnPreferenceChangeListener(this);

            // Get shared preferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            // Get the string value that is associated with the preference's key
            String preferenceString = preferences.getString(preference.getKey(), "");

            // update the preference with the new value (preferenceString)
            onPreferenceChange(preference, preferenceString);
        }

    }

}
