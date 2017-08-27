package com.example.birthdayreminder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Birthday_Today_Activity extends AppCompatActivity {

    private ListView listView_today;
    public static final String EXTRA_ID = "com.example.birthdayreminder.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday__today_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView_today = (ListView)findViewById(R.id.list_view_today);
        listView_today.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(Birthday_Today_Activity.this, ViewContact.class);
                intent.putExtra(EXTRA_ID, cursor.getLong(cursor.getColumnIndex(ContactContract.ContactEntry._ID)));
                Birthday_Today_Activity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ContactDbQueries dbq = new ContactDbQueries(new ContactDbHelper(getApplicationContext()));

        String[] columns = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_NAME_NAME,
                ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY
        };
        //
        String TodayDate = MainActivity.getBirthday(0);
        String selection = ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY + " like ?";
        String[] selectionArgsToday = {"%"+ TodayDate +"%" };
        if(dbq.query(columns, selection, selectionArgsToday, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC") != null)
        {
            Cursor contactList_Today = dbq.query(columns, selection, selectionArgsToday, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC");
            ContactCursorAdapter adapterToday = new ContactCursorAdapter(this, contactList_Today, 0);
            listView_today.setAdapter(adapterToday);
        }
    }

}
