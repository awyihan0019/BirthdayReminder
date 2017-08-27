package com.example.birthdayreminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yihan on 27/8/2017.
 */

public class UpdateService extends IntentService {

    public static final String EXTRA_RANDOM = "com.example.birthdayremainder.BIRTHDAY_NUMBER";

    public UpdateService(String name) {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 24);
        calendar.set(Calendar.SECOND, 20);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void getTodayBithday(){
        Calendar calendar = Calendar.getInstance();
    }


    public void sendNotification(int n) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_RANDOM, n);


        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        int id = 1;
        Notification.Builder nBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.bithday_cake_icon)
                        .setContentTitle("Birthday Today")
                        .setContentText("Bithday Number: " + n)
                        .setContentIntent(resultPendingIntent);

        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.notify(id, nBuilder.build());
    }
}
