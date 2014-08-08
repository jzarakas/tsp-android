package com.labsfunware.tsp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.labsfunware.tsp.AlarmController;
import com.labsfunware.tsp.C;

/**
 * Created by james on 8/7/14.
 */
public class NotificationDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Which do you need?")
                .setPositiveButton("Toilet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       setupNotification(C.ALARM_TYPE_TOILET);
                    }
                })
                .setNegativeButton("Urinal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setupNotification(C.ALARM_TYPE_URINAL);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void setupNotification(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.edit().putString(C.KEY_ALARM_TYPE_PREF, key).apply();
        prefs.edit().putBoolean(C.KEY_ALARM_RUNNING_PREF, true).apply();
        AlarmController.setAlarm(getActivity());
    }
}
