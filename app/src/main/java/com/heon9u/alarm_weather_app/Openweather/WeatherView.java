package com.heon9u.alarm_weather_app.Openweather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.MobileAds;
import com.heon9u.alarm_weather_app.R;

public class WeatherView extends Fragment {

    CardView adContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.weather_view, container, false);
        adContainer = view.findViewById(R.id.adContainer);
        initAdMob();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initAdMob() {
        MobileAds.initialize(getContext(), initializationStatus -> { });


    }
}
