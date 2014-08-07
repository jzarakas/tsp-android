package com.labsfunware.tsp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by james on 8/6/14.
 */
public final class C {

    public static final int URINAL_THRESHOLD = 42000;
    public static final int TOILET_THRESHOLD = 30000;
    public static final int LIGHT_THRESHOLD = 7000;

    public static final long DEFAULT_STATUS_REFRESH = 5000;
    public static final long DEFAULT_ALARM_REFRESH = 5000;

    public static final String KEY_ALARM_TYPE_PREF = "alarm_type";
    public static final String KEY_ALARM_RUNNING_PREF = "alarm_running";
    public static final String KEY_STATUS_REFRESH = "key_status_refresh";
    public static final String KEY_ALARM_REFRESH = "key_alarm_refresh";

    public static final String ALARM_TYPE_URINAL = "urinal";
    public static final String ALARM_TYPE_TOILET = "toilet";

    public static final String TAG_NOTIFICATION_DIALOG = "notif_dialog";

    private C() {
    }

}
