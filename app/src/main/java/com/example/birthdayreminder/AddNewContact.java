package com.example.birthdayreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class AddNewContact extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etBirthday;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private boolean saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences(getResources().getString(R.string.sp_save_state), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        etName = (EditText)AddNewContact.this.findViewById(R.id.input_name);
        etEmail = (EditText)AddNewContact.this.findViewById(R.id.input_email);
        etBirthday = (EditText)AddNewContact.this.findViewById(R.id.input_birthday);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String birthday = etBirthday.getText().toString();

                ContactDbQueries dbq = new ContactDbQueries(new ContactDbHelper(getApplicationContext()));
                Contact contact = new Contact(name, email, birthday);
                if(dbq.insert(contact) != 0) {
                    saved = true;
                }
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(saved) {
            editor.clear();
        }
        else {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String birthday = etBirthday.getText().toString();

            editor.putString("SAVE_STATE_NAME", name);
            editor.putString("SAVE_STATE_EMAIL", email);
            editor.putString("SAVE_STATE_BIRTHDAY", birthday);
        }

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = sharedPref.getString("SAVE_STATE_NAME", "");
        String email = sharedPref.getString("SAVE_STATE_EMAIL", "");
        String birthday = sharedPref.getString("SAVE_STATE_BIRTHDAY", "");

        etName.setText(name);
        etEmail.setText(email);
        etBirthday.setText(birthday);
    }


    public void showDatePickerDialog(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }
}
