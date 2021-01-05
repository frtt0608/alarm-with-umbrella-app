package com.heon9u.alarm_weather_app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.R;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder>{

    String[] langData = {};
    private LayoutInflater layoutInflater;

    AppAdapter(String[] langData) {
        this.langData = langData;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alarm_item, parent, false);

        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        String title = langData[position];
        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return langData.length;
    }

    public class AppViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView title;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            title = itemView.findViewById(R.id.title);
        }
    }
}
