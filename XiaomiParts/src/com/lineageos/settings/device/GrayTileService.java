/*
 * Copyright (C) 2020 CarbonROM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lineageos.settings.device;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class GrayTileService extends TileService {

    private static final String KEY_KCAL_SATURATION = "kcal_saturation";
    private static final String KEY_KCAL_GREYSCALE = "kcal_greyscale";
    private static final String COLOR_FILE_SATURATION = "/sys/devices/platform/kcal_ctrl.0/kcal_sat";

    @Override
    public void onStartListening() {
        boolean storedGreyscale = PreferenceManager.
                getDefaultSharedPreferences(this).getBoolean(KEY_KCAL_GREYSCALE, false);

        Tile tile = getQsTile();
        tile.setState(storedGreyscale ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);

        tile.updateTile();
        super.onStartListening();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean storedGreyscale = prefs.getBoolean(KEY_KCAL_GREYSCALE, false);

        if (storedGreyscale) {
            prefs.edit().putBoolean(KEY_KCAL_GREYSCALE, false).commit();
            String storedSaturation = String.valueOf(prefs.getInt(KEY_KCAL_SATURATION, 255));
            Utils.writeValue(COLOR_FILE_SATURATION, String.valueOf(storedSaturation));
            tile.setState(Tile.STATE_INACTIVE);
        } else {
            prefs.edit().putBoolean(KEY_KCAL_GREYSCALE, true).commit();
            Utils.writeValue(COLOR_FILE_SATURATION, "128");
            tile.setState(Tile.STATE_ACTIVE);
        }

        tile.updateTile();
        super.onClick();
    }
}
