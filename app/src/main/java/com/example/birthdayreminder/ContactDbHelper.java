package com.example.birthdayreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yihan on 2017/8/20.
 */

public class ContactDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contact.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactContract.ContactEntry.TABLE_NAME + " (" +
                    ContactContract.ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactContract.ContactEntry.COLUMN_NAME_NAME + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY + " TEXT)";

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
