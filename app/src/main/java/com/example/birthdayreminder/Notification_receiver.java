package com.example.birthdayreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.NotificationCompat;

/**
 * Created by yihan on 27/8/2017.
 */

public class Notification_receiver extends BroadcastReceiver{

    public Context context;

    @Override
    public void onReceive(Context contextGet, Intent intent) {
        context = contextGet;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, Birthday_Today_Activity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int birthdayToday = getNumberBirthdayToday();
        if(birthdayToday >0) {
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.bithday_cake_icon)
                    .setContentTitle("Birthday Remainder")
                    .setContentText("You have "+birthdayToday+" friend are birthday today!!")
                    .setAutoCancel(true);

            notificationManager.notify(100, builder.build());
        }
        else {
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.bithday_cake_icon)
                    .setContentTitle("Birthday Remainder")
                    .setContentText("You have no friend are birthday today.")
                    .setAutoCancel(true);

            notificationManager.notify(100, builder.build());
        }
    }

    public int getNumberBirthdayToday(){
        ContactDbQueries dbq = new ContactDbQueries(new ContactDbHelper(context));

        String[] columns = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_NAME_NAME,
                ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY
        };
        //
        String TodayDate = MainActivity.getBirthday(0);
        String selection = ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY + " like ?";
        String[] selectionArgsToday = {"%"+ TodayDate +"%" };
        Cursor cursor = dbq.query(columns, selection, selectionArgsToday, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC");

        return cursor.getCount();
    }
}
