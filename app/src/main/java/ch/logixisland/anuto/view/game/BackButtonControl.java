package ch.logixisland.anuto.view.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ch.logixisland.anuto.Preferences;

public class BackButtonControl {

    public enum BackButtonAction {
        DO_NOTHING,
        SHOW_TOAST,
        EXIT
    }

    private enum BackButtonMode {
        DISABLED,
        ENABLED,
        TWICE
    }

    private static final long BACK_TWICE_INTERVAL = 2000;

    private final SharedPreferences mPreferences;

    private long mLastBackButtonPress;

    public BackButtonControl(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public BackButtonAction backButtonPressed() {
        long timeNow = System.currentTimeMillis();
        return BackButtonAction.EXIT;

    }

   // private BackButtonMode getBackButtonMode() {
     //   String backModeString = mPreferences.getString(Preferences.BACK_BUTTON_MODE, null);
//
     //   try {
     //       return BackButtonMode.valueOf(backModeString);
     //   } catch (Exception e) {
     //       return BackButtonMode.DISABLED;
    //    }

        }

