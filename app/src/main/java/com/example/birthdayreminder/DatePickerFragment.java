package com.example.birthdayreminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yihan on 2017/8/20.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        EditText etDate = (EditText)getActivity().findViewById(R.id.input_birthday);
        if (etDate.getText().toString() != null && etDate.getText().toString().hashCode() != 0) {
            String[] YMD = etDate.getText().toString().split("-");
            year = Integer.parseInt(YMD[0]);
            month = Integer.parseInt(YMD[1]) - 1;
            day = Integer.parseInt(YMD[2]);
        }
        // Instantiate DatePickerDialog
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);

        // You may configure your DatePicker here before returning it.
        DatePicker dp = dpd.getDatePicker();

        calendar.add(Calendar.DAY_OF_MONTH, 0);
        dp.setMaxDate(calendar.getTimeInMillis());
        // return DatePickerDialog
        return dpd;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText etDate = (EditText)getActivity().findViewById(R.id.input_birthday);
        etDate.setText(year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
