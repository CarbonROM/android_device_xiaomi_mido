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

public class HallTileService extends TileService {

    private static final String KEY_HALL_WAKEUP = "hall";
    private static final String HALL_WAKEUP_PROP = "persist.service.folio_daemon";

    @Override
    public void onStartListening() {
        boolean hallWakeupData = PreferenceManager.
                getDefaultSharedPreferences(this).getBoolean(KEY_HALL_WAKEUP, false);

        Tile tile = getQsTile();
        tile.setState(hallWakeupData ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);

        tile.updateTile();
        super.onStartListening();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean hallWakeupData = prefs.getBoolean(KEY_HALL_WAKEUP, false);

        // set the inverse value
        tile.setState(!hallWakeupData ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        prefs.edit().putBoolean(KEY_HALL_WAKEUP, !hallWakeupData).commit();
        Utils.setProp(HALL_WAKEUP_PROP, !hallWakeupData);

        tile.updateTile();
        super.onClick();
    }
}
