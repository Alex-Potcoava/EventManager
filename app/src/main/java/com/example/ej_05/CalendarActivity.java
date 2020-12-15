package com.example.ej_05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class CalendarActivity extends AppCompatActivity {

    Button addDate, addHour;
    EditText setDate, setHour;
    private int day, month, year, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        addDate = findViewById(R.id.addDate);
        addHour = findViewById(R.id.addHour);
        setDate = findViewById(R.id.setDate);
        setHour = findViewById(R.id.setHour);

    }

    public void addDate(View view){
        if(view == addDate){
            final java.util.Calendar calendar = java.util.Calendar.getInstance();
            day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
            month = calendar.get(java.util.Calendar.MONTH);
            year = calendar.get(java.util.Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    setDate.setText(dayOfMonth + "/" + (month +1 ) + "/" + year);
                }
            }
            ,day, month, year);
            datePickerDialog.show();
        }

        if (view == addHour){
            final java.util.Calendar calendar = java.util.Calendar.getInstance();
            hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
            minute = calendar.get(java.util.Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    setHour.setText(hourOfDay + ":" + minute);
                }
            }
            , hour, minute, false);
            timePickerDialog.show();
        }
    }

    public void changeEventActivity(View view){
        if (setDate.getText().toString().equals("") || setHour.getText().toString().equals("")){
            View view1 = findViewById(R.id.calendar_layout);
            Snackbar.make(view1, getString(R.string.empty_text), Snackbar.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, EventActivity.class);
            intent.putExtra("Date", setDate.getText().toString());
            intent.putExtra("Hour", setHour.getText().toString());
            startActivity(intent);
            finish();
        }
    }
}