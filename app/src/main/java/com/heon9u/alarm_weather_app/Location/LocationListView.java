package com.heon9u.alarm_weather_app.Location;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heon9u.alarm_weather_app.AnotherTools.AdBannerClass;
import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class LocationListView extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Location> locationList;
    FloatingActionButton createLocation;
    LocationDatabase locationDB;
    LocationAdapter locationAdapter;
    RecyclerView recyclerView;
    TextView noLocationText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_view);

        locationDB = new LocationDatabase(this);
        noLocationText = findViewById(R.id.noLocationText);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        createLocation = findViewById(R.id.createLocation);
        createLocation.setOnClickListener(this);

        takeAdapter();
        initAdMob();
    }

    public void takeAdapter() {
        displayLocation();
        locationAdapter = new LocationAdapter(this,
                this,
                locationList);
        recyclerView.setAdapter(locationAdapter);
    }

    public void displayLocation() {
        locationList = locationDB.readAllLocation();

        if(locationList.size() == 0) {
            noLocationText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noLocationText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        locationDB.close();
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

    public void initAdMob() {
        MobileAds.initialize(this, initializationStatus -> { });

        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        Display display = getWindowManager().getDefaultDisplay();
        AdBannerClass adBannerClass = new AdBannerClass(getApplicationContext(), display);
        frameLayout.addView(adBannerClass.adView);
    }
}
