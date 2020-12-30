package com.lineageos.settings.device;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class GrayTileService extends TileService {

    public static final String KEY_KCAL_SATURATION = "kcal_saturation";
    private static final String COLOR_FILE_SATURATION = "/sys/devices/platform/kcal_ctrl.0/kcal_sat";

    @Override
    public void onStartListening() {
        boolean storedGreyscale = PreferenceManager.
                getDefaultSharedPreferences(this).getBoolean(DisplayCalibration.KEY_KCAL_GREYSCALE, false);

        Tile tile = getQsTile();
        if (storedGreyscale) {
            tile.setState(Tile.STATE_ACTIVE);
        } else {
            tile.setState(Tile.STATE_INACTIVE);
        }

        tile.updateTile();
        super.onStartListening();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean storedGreyscale = prefs.getBoolean(DisplayCalibration.KEY_KCAL_GREYSCALE, false);

        if (storedGreyscale) {
            prefs.edit().putBoolean(DisplayCalibration.KEY_KCAL_GREYSCALE, false).commit();
            String storedSaturation = String.valueOf(prefs.getInt(KEY_KCAL_SATURATION, 255));
            Utils.writeValue(COLOR_FILE_SATURATION, String.valueOf(storedSaturation));
            tile.setState(Tile.STATE_INACTIVE);
        } else {
            prefs.edit().putBoolean(DisplayCalibration.KEY_KCAL_GREYSCALE, true).commit();
            Utils.writeValue(COLOR_FILE_SATURATION, "128");
            tile.setState(Tile.STATE_ACTIVE);
        }

        tile.updateTile();
        super.onClick();
    }
}
