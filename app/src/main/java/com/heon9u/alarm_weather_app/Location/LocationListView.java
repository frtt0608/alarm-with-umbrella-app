package com.heon9u.alarm_weather_app.Location;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heon9u.alarm_weather_app.AnotherTools.AdBannerClass;
import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;
import java.util.Collections;

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

        initAdMob();
        takeAdapter();
        attachItemTouchHelperToAdapter();
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
            hideLocationList();
        } else {
            showLocationList();
        }

        locationDB.close();
    }

    public void attachItemTouchHelperToAdapter() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getBindingAdapterPosition();
                int toPos = target.getBindingAdapterPosition();
                Collections.swap(locationList, fromPos, toPos);
                recyclerView.getAdapter().notifyItemMoved(fromPos, toPos);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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

        FrameLayout frameLayout = findViewById(R.id.AdMobLayout);
        Display display = getWindowManager().getDefaultDisplay();
        AdBannerClass adBannerClass = new AdBannerClass(getApplicationContext(), display);
        frameLayout.addView(adBannerClass.adView);
    }

    public void hideLocationList() {
        noLocationText.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void showLocationList() {
        noLocationText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
