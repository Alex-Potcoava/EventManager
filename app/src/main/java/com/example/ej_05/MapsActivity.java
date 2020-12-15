package com.example.ej_05;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, CustomAdapter.OnButtonClickListener{

    private GoogleMap mMap;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    public static ArrayList<String> title = new ArrayList<>();
    public String eventPlace;
    public String eventName;
    public String eventDate;
    public Geocoder geocoder;
    private Marker currentLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (SaveList.readListFromPref(this) != null){
            title =  SaveList.readListFromPref(this);
        }
        
        Intent eventIntent = getIntent();
        String event = eventIntent.getStringExtra("Event title");
        eventPlace = eventIntent.getStringExtra("Event place");
        eventName = eventIntent.getStringExtra("Event name");
        eventDate = eventIntent.getStringExtra("Event date");

        recyclerView = findViewById(R.id.eventList);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (title.size() != 0 && event == null) {
            addEvent(title);
        }else{
            if(event != null){
                addEvent(event);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        activateLocation();
        if (eventPlace != null){
            addMarker(eventPlace, eventName, eventDate);
        }
    }

    public void activateLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String permission = (Manifest.permission.ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{permission}, 123);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 123 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            activateLocation();
        }
    }

    public void addMarker(String eventPlace, String eventName, String eventDate){
        if (geocoder == null){
            geocoder = new Geocoder(this);
        }
        try {
            List<Address> addresses = geocoder.getFromLocationName(eventPlace, 1);
            if(addresses.size() != 0){
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(eventName).snippet(eventDate));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeCalendarActivity(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
        finish();
    }

    public void addEvent(ArrayList<String> title){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this, title);
        recyclerView.setAdapter(adapter);
    }

    public void addEvent(String event){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        title.add(event);
        SaveList.writeListInPref(this,title);
        adapter = new CustomAdapter(this,title);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void removeEvent(int position) {
        title.remove(position);
        adapter = new CustomAdapter(this, title);

        recyclerView.setAdapter(adapter);

        if (null != mMap) {
            mMap.clear();
        }
    }

    @Override
    public void addMarkerEvent(int position) {
        String split = title.get(position);
        String[] getPlace = split.split("'");
        if (geocoder == null){
            geocoder = new Geocoder(this);
        }
        try {
            List<Address> addresses = geocoder.getFromLocationName(getPlace[1], 1);
            if(addresses.size() != 0){
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                if (null != currentLocationMarker) {
                    currentLocationMarker.remove();
                }
                currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}