package com.heon9u.alarm_weather_app.Location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    Context context;
    Activity activity;
    List<Location> locationList;

    LocationAdapter(Context context,
                    Activity activity,
                    ArrayList locationList) {
        this.context = context;
        this.activity = activity;
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
        String address = location.getStreetAddress();
        if(address == null) {
            address = location.getLotAddress();
        }
        holder.address.setText(address);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.getCallingActivity() == null) {
                    // AlarmListView -> Toast
                    Toast.makeText(context, location.getStreetAddress(), Toast.LENGTH_SHORT).show();
                } else {
                    // AlarmSetActivity -> StartActivityForResult
                    Intent intent = new Intent();
                    intent.putExtra("location", location);
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                }
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // delete
                deleteDialog(location);
                return true;
            }
        });
    }

    public void deleteDialog(Location location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete??");
        builder.setIcon(android.R.drawable.ic_menu_delete);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocationDatabase locationDB = new LocationDatabase(context);
                int result = locationDB.deleteLocation(location.getId());

                if(result > 0) {
                    locationList.remove(location);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "delete error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
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
