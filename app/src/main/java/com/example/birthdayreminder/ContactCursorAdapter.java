package com.example.birthdayreminder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by yihan on 2017/8/20.
 */

public class ContactCursorAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    public ContactCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView)view.findViewById(R.id.item_name);
        TextView tvBirthday = (TextView)view.findViewById(R.id.item_birthday);
        String name = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_NAME_NAME));
        String birthday = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY));
        tvName.setText(name);
        tvBirthday.setText(birthday);
    }
}
