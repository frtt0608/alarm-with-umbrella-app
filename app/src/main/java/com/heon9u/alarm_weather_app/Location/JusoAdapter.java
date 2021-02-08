package com.heon9u.alarm_weather_app.Location;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JusoAdapter extends RecyclerView.Adapter<JusoAdapter.JusoViewHolder> {

    Context context;
    List<Location> jusoList;

    JusoAdapter(Context context, ArrayList jusoList) {
        this.context = context;
        this.jusoList = jusoList;
    }

    @NonNull
    @Override
    public JusoAdapter.JusoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.juso_item, parent, false);

        return new JusoAdapter.JusoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JusoAdapter.JusoViewHolder holder, int position) {
        Location juso = jusoList.get(position);
        holder.streetAddress.setText(juso.getStreetAddress());
        holder.lotAddress.setText(juso.getLotAddress());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGeocoding(juso);
                LocationDatabase locationDB = new LocationDatabase(context);
                locationDB.createLocation(juso);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jusoList.size();
    }

    public static class JusoViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView streetAddress, lotAddress;

        public JusoViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            streetAddress = itemView.findViewById(R.id.streetAddress);
            lotAddress = itemView.findViewById(R.id.lotAddress);
        }
    }

    public void setGeocoding(Location juso) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> address;
        try {
            // 도로명주소가 없을 때는 지번주소 이용하기.
            if(juso.getStreetAddress() == null) {
                address = geocoder.getFromLocationName(juso.getLotAddress(), 1);
            } else {
                address = geocoder.getFromLocationName(juso.getStreetAddress(), 1);
            }

            juso.setLatitude(address.get(0).getLatitude());
            juso.setLongitude(address.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
