package com.example.birthdayreminder;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by yihan on 27/8/2017.
 */

public class PostJsonTask extends AsyncTask<Void, Void, JSONObject> {

    private static final String JSON_URL = "http://labs.jamesooi.com/uecs3253-asg.php";

    private MainActivity activity;

    public PostJsonTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject response = null;
        try {
            response = postJson();
        }
        catch (IOException ex) {
            Log.e("IO_EXCEPTION", ex.toString());
        }

        return response;
    }
    @Override
    protected void onPostExecute(JSONObject response) {
        try {
            Log.d("RECORDS_SYNCED", Integer.toString(response.getInt("recordsSynced")));
        }
        catch (Exception ex) {
            Log.e("JSON_EXCEPTION", ex.toString());
        }
    }
    private JSONObject postJson() throws IOException {
        InputStream is = null;
        OutputStream os;
        String[] columns = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_NAME_NAME,
                ContactContract.ContactEntry.COLUMN_NAME_BIRTHDAY,
                ContactContract.ContactEntry.COLUMN_NAME_EMAIL
        };
        Cursor cursor = MainActivity.dbq.query(columns, null, null, null, null, ContactContract.ContactEntry.COLUMN_NAME_NAME + " ASC");
        JSONArray contactData = new JSONArray();
        if (cursor.moveToFirst()){
            do{
                JSONObject jsonObject = new JSONObject();
                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                try {
                    jsonObject.put("id",id);
                    jsonObject.put("name",name);
                    jsonObject.put("birthday",birthday);
                    jsonObject.put("email",email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                contactData.put(jsonObject);
                // do what ever you want here
            }while(cursor.moveToNext());
        }
        cursor.close();

        try {

            URL url = new URL(JSON_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Starts the query
            os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(contactData));
            writer.flush();
            writer.close();
            os.close();


            int responseCode = conn.getResponseCode();
            if(responseCode == 200) {
                is = conn.getInputStream();

                // Convert the InputStream into ArrayList<Person>
                return readInputStream(is);
            }
            else {
                Log.e("HTTP_ERROR", Integer.toString(responseCode));
                return null;
            }
        }
        catch (Exception ex) {
            Log.e("EXCEPTION", ex.toString());
            return null;
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public JSONObject readInputStream(InputStream is)
            throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder builder = new StringBuilder();

        String input;
        while ((input = reader.readLine()) != null)
            builder.append(input);

        return new JSONObject(builder.toString());
    }

    private String getPostDataString(JSONArray data) throws Exception {

        StringBuilder result = new StringBuilder();

        result.append(URLEncoder.encode("data", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(data.toString(), "UTF-8"));

        return result.toString();
    }
}
