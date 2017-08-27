package com.example.birthdayreminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yihan on 2017/8/20.
 */

public class ContactDbQueries {
    private ContactDbHelper helper;


    public ContactDbQueries(ContactDbHelper helper) {
        this.helper = helper;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.query(
                ContactContract.ContactEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
    }

    public long insert(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactContract.ContactEntry.COLUMN_NAME_EMAIL, contact.getEmail());
        values.put(ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY, contact.getBirthday());

        long id = db.insert(ContactContract.ContactEntry.TABLE_NAME, null, values);
        contact.setId(id);
        return id;
    }

    public int update(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactContract.ContactEntry.COLUMN_NAME_EMAIL, contact.getEmail());
        values.put(ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY, contact.getBirthday());

        String selection = ContactContract.ContactEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(contact.getId())};

        return db.update(
                ContactContract.ContactEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = ContactContract.ContactEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};
        db.delete(ContactContract.ContactEntry.TABLE_NAME, selection, selectionArgs);
    }
}
