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
