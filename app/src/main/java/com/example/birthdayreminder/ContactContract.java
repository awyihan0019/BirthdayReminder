package com.example.birthdayreminder;

import android.provider.BaseColumns;

/**
 * Created by yihan on 2017/8/20.
 */

public class ContactContract {

    private ContactContract(){}

    public static class ContactEntry implements BaseColumns{
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BIRTHDAY = "birthday";
        public static final String COLUMN_NAME_EMAIL = "email";
    }
}
