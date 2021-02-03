package com.heon9u.alarm_weather_app.Location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    Context context;
    List<Location> locationList;

    LocationAdapter(Context context, ArrayList locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.location_item, parent, false);

        return new LocationAdapter.LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.address.setText(location.getAddress());
        holder.cardView.setOnClickListener(new cardViewClickListener());
    }

    public class cardViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView address;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            address = itemView.findViewById(R.id.address);
        }
    }
}
