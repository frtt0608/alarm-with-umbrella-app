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
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_view);

        locationDB = new LocationDatabase(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        takeAdapter();

        createLocation = findViewById(R.id.createLocation);
        createLocation.setOnClickListener(this);
    }

    public void takeAdapter() {
        displayLocation();
        LocationAdapter locationAdapter = new LocationAdapter(getApplicationContext(),
                this,
                locationList);
        recyclerView.setAdapter(locationAdapter);
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
        location.setStreetAddress(cursor.getString(1));
        location.setLotAddress(cursor.getString(2));
        location.setCommunityCenter(cursor.getString(3));
        location.setLatitude(cursor.getDouble(4));
        location.setLongitude(cursor.getDouble(5));

        return location;
    }

    @Override
    protected void onStart() {
        super.onStart();
        takeAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createLocation:
                Intent jusoCreateIntent = new Intent(getApplicationContext(), JusoCreateActivity.class);
                startActivity(jusoCreateIntent);
                break;
        }
    }
}
