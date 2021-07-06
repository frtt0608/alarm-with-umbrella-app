package com.heon9u.alarm_weather_app.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.MobileAds;
import com.heon9u.alarm_weather_app.anotherTools.AdBannerClass;
import com.heon9u.alarm_weather_app.databinding.LocationViewBinding;
import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.R;
import com.heon9u.alarm_weather_app.location.database.LocationViewModel;

import java.util.List;

public class LocationListView extends AppCompatActivity {
    public static final int CREATE_LOCATION_REQUEST = 11;

    LocationViewBinding locationViewBinding;
    LocationViewModel locationViewModel;
    LocationAdapter locationAdapter;

    RecyclerView recyclerView;
    TextView noLocationText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_view);
        locationViewBinding = DataBindingUtil.setContentView(this, R.layout.location_view);
        locationViewBinding.setLocationView(this);

        noLocationText = findViewById(R.id.noLocationText);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationAdapter = new LocationAdapter();
        recyclerView.setAdapter(locationAdapter);

        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel.getAllLocations().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                locationAdapter.submitList(locations);

                if(locations.size() > 0) showLocationList();
                else hideLocationList();
            }
        });

        locationAdapter.setOnItemClickListener(location -> {
            if(getCallingActivity() == null) {
                // AlarmListView -> Toast
                Toast.makeText(getApplicationContext(), location.getLotAddress(), Toast.LENGTH_SHORT).show();
            } else {
                // AlarmSetActivity -> StartActivityForResult
                Intent resultIntent = new Intent();
                resultIntent.putExtra("location", location);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        initAdMob();
        attachItemTouchHelperToAdapter();
    }

    public void attachItemTouchHelperToAdapter() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                showDialogDeleteLocation(viewHolder);
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void showDialogDeleteLocation(@NonNull RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주소 삭제")
                .setMessage("해당 주소를 삭제하겠습니까??")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setCancelable(false)
                .setPositiveButton("삭제", (dialog, which) -> {
                    locationViewModel.delete(locationAdapter.getLocationAt(viewHolder.getBindingAdapterPosition()));
                    Toast.makeText(getApplicationContext(), "저장한 주소를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("취소", (dialog, which) -> {
                    locationAdapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
                    Toast.makeText(getApplicationContext(), "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                });
        builder.show();
    }

    public void createLocationActivity(View view) {
        Intent jusoIntent = new Intent(getApplicationContext(), JusoCreateActivity.class);
        startActivityForResult(jusoIntent, CREATE_LOCATION_REQUEST);
    }

    public void initAdMob() {
        MobileAds.initialize(this, initializationStatus -> { });

        FrameLayout frameLayout = findViewById(R.id.AdMobLayout);
        Display display = getWindowManager().getDefaultDisplay();
        AdBannerClass adBannerClass = new AdBannerClass(getApplicationContext(), display);
        frameLayout.addView(adBannerClass.adView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;

        if(requestCode == CREATE_LOCATION_REQUEST) {
            Location location = (Location) data.getSerializableExtra("Location");
            Log.e("LocationListView", location.toString());
            locationViewModel.insert(location);
        }
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
