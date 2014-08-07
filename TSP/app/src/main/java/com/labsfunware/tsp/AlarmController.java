package com.labsfunware.tsp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.labsfunware.tsp.api.BoardStatus;
import com.labsfunware.tsp.api.TSPClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by james on 8/7/14.
 */
public class AlarmController extends BroadcastReceiver {
    public static void setAlarm(Context c) {
        cancelAlarm(c);

        Log.d("ANOTIF", "setting alarm!");

        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + C.DEFAULT_ALARM_REFRESH, getPendingIntent(c));
    }

    public static void cancelAlarm(Context c) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        am.cancel(getPendingIntent(c));
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent i = new Intent(context, AlarmController.class);
        return PendingIntent.getBroadcast(context, 0, i, 0);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("ANOTIF", "onReceive()");
        final String alarmType = PreferenceManager.getDefaultSharedPreferences(context).getString(C.KEY_ALARM_TYPE_PREF, "");
        final boolean alarmRunning = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(C.KEY_ALARM_RUNNING_PREF, false);

        //alarm shouldn't be running
        if (!alarmRunning || alarmType.equals("")) {
            Log.d("ANOTIF", "alarm shouldnt be running - " + alarmRunning + " - " + alarmType);
            cancelAlarm(context);
            return;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://agent.electricimp.com")
                .build();

        TSPClient tspClient = restAdapter.create(TSPClient.class);
        tspClient.getStatus(new Callback<BoardStatus>() {
            @Override
            public void success(BoardStatus boardStatus, Response response) {

                Log.d("ANOTIF", "got api response + parsed");
                Log.d("ANOTIF", "alarmType - " + alarmType + " - pin: " + boardStatus.getPin2());
                if (alarmType.equals(C.ALARM_TYPE_TOILET) && boardStatus.getPin2() > C.TOILET_THRESHOLD) {
                    //toilet is available
                    fireNotification(context, "toilet");
                } else if (alarmType.equals(C.ALARM_TYPE_URINAL) && boardStatus.getPin1() > C.URINAL_THRESHOLD) {
                    //urinal is available
                    fireNotification(context, "urinal");
                } else {
                    setAlarm(context);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("RETROFIT_ERROR", error.getMessage());
                Log.d("ANOTIF", "resetting alarm!");
                setAlarm(context);
            }
        });

    }

    private void fireNotification(Context context, String alarmType) {
        Log.d("ANOTIF", "firing notification!");
        Notification.Builder notif = new Notification.Builder(context);
        notif.setContentTitle("Clarendon TSP");
        notif.setContentText("The " + alarmType + " is available!");
        notif.setSmallIcon(R.drawable.ic_launcher);
        notif.setDefaults(Notification.DEFAULT_ALL);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, notif.build());

    }
}
