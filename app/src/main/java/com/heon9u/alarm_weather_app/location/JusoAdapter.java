package com.heon9u.alarm_weather_app.location;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.databinding.JusoItemBinding;
import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.R;

import java.util.List;

public class JusoAdapter extends RecyclerView.Adapter<JusoAdapter.JusoViewHolder> {

    OnItemClickListener listener;
    List<Location> resultLocations;

    JusoAdapter(List resultLocations) {
        this.resultLocations = resultLocations;
    }

    @NonNull
    @Override
    public JusoAdapter.JusoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        JusoItemBinding jusoItemBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.juso_item, parent, false);

        return new JusoAdapter.JusoViewHolder(jusoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull JusoAdapter.JusoViewHolder holder, int position) {
        Location curLocation = resultLocations.get(position);
        holder.jusoItemBinding.setLocation(curLocation);
    }

    @Override
    public int getItemCount() {
        return resultLocations.size();
    }

    public class JusoViewHolder extends RecyclerView.ViewHolder {
        JusoItemBinding jusoItemBinding;

        public JusoViewHolder(@NonNull JusoItemBinding jusoItemBinding) {
            super(jusoItemBinding.getRoot());
            this.jusoItemBinding = jusoItemBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    listener.onItemClick(resultLocations.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Location location);
    }

    public void setOnItemClickListener(JusoAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
