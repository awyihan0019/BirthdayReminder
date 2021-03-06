package com.example.birthdayreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.birthdayreminder.ID";
    protected static Cursor contactList_Today;
    protected static Cursor contactList_Tomorrow;
    protected static Cursor contactList_DayAfter;
    protected static ContactDbQueries dbq;
    protected String response ;
    private ListView listView_today;
    private ListView listView_tomorrow;
    private ListView listView_dayAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewContact.class);
                startActivity(intent);
            }
        });
        //

        listView_today = (ListView)findViewById(R.id.list_view_today);
        listView_today.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ViewContact.class);
                intent.putExtra(EXTRA_ID, cursor.getLong(cursor.getColumnIndex(ContactContract.ContactEntry._ID)));
                MainActivity.this.startActivity(intent);
            }
        });

        listView_tomorrow = (ListView)findViewById(R.id.list_view_tomorrow);
        listView_tomorrow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ViewContact.class);
                intent.putExtra(EXTRA_ID, cursor.getLong(cursor.getColumnIndex(ContactContract.ContactEntry._ID)));
                MainActivity.this.startActivity(intent);
            }
        });

        listView_dayAfter = (ListView)findViewById(R.id.list_view_dayAfter);
        listView_dayAfter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ViewContact.class);
                intent.putExtra(EXTRA_ID, cursor.getLong(cursor.getColumnIndex(ContactContract.ContactEntry._ID)));
                MainActivity.this.startActivity(intent);
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();

        dbq = new ContactDbQueries(new ContactDbHelper(getApplicationContext()));

        String[] columns = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_NAME_NAME,
                ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY
        };
        //
        String TodayDate = getBirthday(0);
        String TomorrowDate = getBirthday(1);
        String selection = ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY + " like ?";
        String[] selectionArgsToday = {"%"+ TodayDate +"%" };
        String[] selectionArgsTomorrow= {"%"+ TomorrowDate +"%"};
        if(dbq.query(columns, selection, selectionArgsToday, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC") != null)
        {
            contactList_Today = dbq.query(columns, selection, selectionArgsToday, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC");
            ContactCursorAdapter adapterToday = new ContactCursorAdapter(this, contactList_Today, 0);
            listView_today.setAdapter(adapterToday);
            setListViewHeight(listView_today);
        }

        if (dbq.query(columns, selection, selectionArgsTomorrow, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC") != null)
        {
            contactList_Tomorrow = dbq.query(columns, selection, selectionArgsTomorrow, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC");
            ContactCursorAdapter adapterTomorrow = new ContactCursorAdapter(this, contactList_Tomorrow, 0);

            listView_tomorrow.setAdapter(adapterTomorrow);
            setListViewHeight(listView_tomorrow);
        }

        contactList_DayAfter = dbq.query(columns, null, null, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC");
        ContactCursorAdapter adapterDayAfter = new ContactCursorAdapter(this, contactList_DayAfter, 0);

        listView_dayAfter.setAdapter(adapterDayAfter);
        setListViewHeight(listView_dayAfter);

        setNotification();
    }

    public void setNotification(){
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public static String getBirthday(int value){
        String birthday;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        //find today value set to 0 , find tomorrow value set to 1
        int day = calendar.get(Calendar.DAY_OF_MONTH) + value;
        birthday = String.valueOf(month) + "-" + String.valueOf(day);

        return birthday;
    }

    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);   //get every item view
            listItem.measure(0, 0);   //compare widthMeasureSpec and heightMeasureSpec if not equal then using onMeasure()
            totalHeight += listItem.getMeasuredHeight();   //plussing
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));   //add every item space
        listView.setLayoutParams(params);
    }

    public void backUpContact()
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {

            new PostJsonTask(MainActivity.this).execute();
            Toast toast = Toast.makeText(MainActivity.this, "Backup Successful " + response, Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.network_unavailable), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.backup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.backup_birthday:
                backUpContact();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
