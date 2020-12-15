package com.example.ej_05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    public TextInputLayout eventName, eventPlace;
    public String date;
    public String hour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventName = findViewById(R.id.chooseTitle);
        eventPlace = findViewById(R.id.chooseEventPlace);

        Intent calendarIntent = getIntent();
        date = calendarIntent.getStringExtra("Date");
        hour = calendarIntent.getStringExtra("Hour");

    }

    public void changeMapsActivity(View view){
        if (eventName.getEditText().getText().toString().equals("") || eventPlace.getEditText().getText().toString().equals("")){
            View view1 = findViewById(R.id.event_layout);
            Snackbar.make(view1, getString(R.string.empty_text), Snackbar.LENGTH_SHORT).show();
            if (eventName.getEditText().getText().toString().equals("")){
                eventName.setError(getString(R.string.empty_text));
            } else {
                eventName.setErrorEnabled(false);
            }
            if (eventPlace.getEditText().getText().toString().equals("")){
                eventPlace.setError(getString(R.string.empty_text));
            } else {
                eventPlace.setErrorEnabled(false);
            }
        } else {
            Intent intent = new Intent(this, MapsActivity.class);
            String eventAndDate = eventName.getEditText().getText().toString()
                    + " en '"
                    + eventPlace.getEditText().getText().toString()
                    + "' \n"
                    + date
                    + " "
                    + hour;
            intent.putExtra("Event title", eventAndDate);
            intent.putExtra("Event place", eventPlace.getEditText().getText().toString());
            intent.putExtra("Event name", eventName.getEditText().getText().toString());
            intent.putExtra("Event date", date + " " + hour);
            startActivity(intent);
            finish();
        }
    }

}