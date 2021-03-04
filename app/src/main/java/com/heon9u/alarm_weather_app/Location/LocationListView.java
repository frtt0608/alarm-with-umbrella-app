package com.heon9u.alarm_weather_app.Location;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class LocationListView extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Location> locationList;
    ImageButton createLocation;
    LocationDatabase locationDB;
    LocationAdapter locationAdapter;
    RecyclerView recyclerView;
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_view);

        locationDB = new LocationDatabase(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        takeAdapter();
        initAdMob();
        createLocation = findViewById(R.id.createLocation);
        createLocation.setOnClickListener(this);
    }

    public void takeAdapter() {
        displayLocation();
        locationAdapter = new LocationAdapter(this,
                this,
                locationList);
        recyclerView.setAdapter(locationAdapter);
    }

    public void displayLocation() {
        locationList = new ArrayList<>();
        Cursor cursor = locationDB.readAllLocation();

        if(cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(),
                    "저장된 주소가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                Location location = setLocation(cursor);
                locationList.add(location);
            }
        }

        cursor.close();
        locationDB.close();
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

    public void initAdMob() {
        MobileAds.initialize(this, initializationStatus -> { });

        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.sample_banner));
        frameLayout.addView(adView);
        loadBanner();
    }

    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
