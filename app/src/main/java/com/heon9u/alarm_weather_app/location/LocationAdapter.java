package com.heon9u.alarm_weather_app.location;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.databinding.LocationItemBinding;
import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.R;

public class LocationAdapter extends ListAdapter<Location, LocationAdapter.LocationViewHolder> {

    public OnItemClickListener listener;

    public LocationAdapter() { super(DIFF_CALLBACK); }

    private static final DiffUtil.ItemCallback<Location> DIFF_CALLBACK = new DiffUtil.ItemCallback<Location>() {
        @Override
        public boolean areItemsTheSame(@NonNull Location oldItem, @NonNull Location newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Location oldItem, @NonNull Location newItem) {
            return oldItem.getStreetAddress().equals(newItem.getStreetAddress()) &&
                    oldItem.getOrderNum() == newItem.getOrderNum();
        }
    };

    @NonNull
    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LocationItemBinding locationItemBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.location_item, parent, false);
        return new LocationAdapter.LocationViewHolder(locationItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder holder, int position) {
        Location location = getItem(position);
        holder.locationItemBinding.setLocation(location);
    }

    public Location getLocationAt(int position) { return getItem(position); }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        LocationItemBinding locationItemBinding;

        public LocationViewHolder(@NonNull LocationItemBinding locationItemBinding) {
            super(locationItemBinding.getRoot());
            this.locationItemBinding = locationItemBinding;

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Location location);
    }

    public void setOnItemClickListener(LocationAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
