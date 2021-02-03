package com.heon9u.alarm_weather_app.Location;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class LocationListView extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Location> locationList;
    ImageButton createLocation;
    LocationDatabase locationDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_view);

        locationDB = new LocationDatabase(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LocationAdapter locationAdapter = new LocationAdapter(getApplicationContext(), locationList);

        createLocation = findViewById(R.id.createLocation);
        createLocation.setOnClickListener(this);
    }

    public void displayLocation() {
        locationList = new ArrayList<>();
        Cursor cursor = locationDB.readAllLocation();

        if(cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No Location", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                Location location = setLocation(cursor);
                locationList.add(location);
            }
        }
    }

    public Location setLocation(Cursor cursor) {
        Location location = new Location();

        location.setId(cursor.getInt(0));
        location.setAddress(cursor.getString(1));
        location.setLatitude(cursor.getDouble(2));
        location.setLongitude(cursor.getDouble(3));

        return location;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createLocation:
                Intent createLocationIntent = new Intent(getApplicationContext(), LocationCreateActivity.class);

                break;
        }
    }
}
