/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.com/licenses/>.
*
*/
package com.lineageos.settings.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceManager;
import androidx.preference.TwoStatePreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String KEY_VIBSTRENGTH = "vib_strength";
    public static final String KEY_YELLOW_TORCH_BRIGHTNESS = "yellow_torch_brightness";
    public static final String KEY_WHITE_TORCH_BRIGHTNESS = "white_torch_brightness";
    public static final String KEY_GLOVE_MODE = "glove_mode";
    public static final String KEY_HALL_WAKEUP = "hall";

    private VibratorStrengthPreference mVibratorStrength;
    private YellowTorchBrightnessPreference mYellowTorchBrightness;
    private WhiteTorchBrightnessPreference mWhiteTorchBrightness;
    private TwoStatePreference mGloveMode;
    private TwoStatePreference mHallWakeup;

    private static final String GLOVE_MODE_FILE = "/sys/devices/virtual/tp_glove/device/glove_enable";
    private static final String HALL_WAKEUP_PROP = "vendor.persist.hall_wakeup";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        PreferenceScreen mKcalPref = (PreferenceScreen) findPreference("kcal");
        mKcalPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
             @Override
             public boolean onPreferenceClick(Preference preference) {
                 Intent intent = new Intent(getActivity().getApplicationContext(), DisplayCalibration.class);
                 startActivity(intent);
                 return true;
             }
        });

        mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        if (mVibratorStrength != null) {
            mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        }

        mYellowTorchBrightness = (YellowTorchBrightnessPreference) findPreference(KEY_YELLOW_TORCH_BRIGHTNESS);
        if (mYellowTorchBrightness != null) {
            mYellowTorchBrightness.setEnabled(YellowTorchBrightnessPreference.isSupported());
        }

        mWhiteTorchBrightness = (WhiteTorchBrightnessPreference) findPreference(KEY_WHITE_TORCH_BRIGHTNESS);
        if (mWhiteTorchBrightness != null) {
            mWhiteTorchBrightness.setEnabled(WhiteTorchBrightnessPreference.isSupported());
        }

        mGloveMode = (TwoStatePreference) findPreference(KEY_GLOVE_MODE);
        mGloveMode.setChecked(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(DeviceSettings.KEY_GLOVE_MODE, false));
        mGloveMode.setOnPreferenceChangeListener(this);

        mHallWakeup = (TwoStatePreference) findPreference(KEY_HALL_WAKEUP);
        mHallWakeup.setChecked(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(DeviceSettings.KEY_HALL_WAKEUP, false));
        mHallWakeup.setOnPreferenceChangeListener(this);
    }

    public static void restore(Context context) {
        boolean gloveModeData = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.KEY_GLOVE_MODE, false);
        Utils.writeValue(GLOVE_MODE_FILE, gloveModeData ? "1" : "0");

        boolean hallWakeupData = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.KEY_HALL_WAKEUP, false);
        Utils.setProp(HALL_WAKEUP_PROP, hallWakeupData);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mGloveMode) {
            Boolean enabled = (Boolean) newValue;
            SharedPreferences.Editor prefChange = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            prefChange.putBoolean(KEY_GLOVE_MODE, enabled).commit();
            Utils.writeValue(GLOVE_MODE_FILE, enabled ? "1" : "0");
        }
        if (preference == mHallWakeup) {
            boolean enabled = (boolean) newValue;
            SharedPreferences.Editor prefChange = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            prefChange.putBoolean(KEY_HALL_WAKEUP, enabled).commit();
            Utils.setProp(HALL_WAKEUP_PROP, enabled);
        }
        return true;
    }
}
