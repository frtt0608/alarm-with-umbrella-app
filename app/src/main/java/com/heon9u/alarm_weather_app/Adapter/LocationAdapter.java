package com.heon9u.alarm_weather_app.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Activity.AppAdapter;
import com.heon9u.alarm_weather_app.Dto.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    List<Location> locationList;

    @NonNull
    @Override
    public AppAdapter.AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AppAdapter.AppViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
