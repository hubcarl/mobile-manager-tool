
package com.blue.sky.mobile.manager.music.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.blue.sky.mobile.manager.R;

public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Load settings XML
        int preferencesResId = R.xml.music_settings;
        addPreferencesFromResource(preferencesResId);
        super.onActivityCreated(savedInstanceState);
    }
}
