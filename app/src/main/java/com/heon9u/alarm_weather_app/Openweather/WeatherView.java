package com.heon9u.alarm_weather_app.Openweather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.MobileAds;
import com.heon9u.alarm_weather_app.Location.LocationDatabase;
import com.heon9u.alarm_weather_app.R;
import com.heon9u.alarm_weather_app.Dto.*;

public class WeatherView extends Fragment {

    CurrentWeather currentWeather;
    HourlyWeather hourlyWeather;
    DailyWeather dailyWeather;
    Location curLocation;
    TextView address;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("WeatherView", "onCreate");
        getLocationData();
        getWeatherData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Log.e("WeatherView", "onCreateView");
        View view = inflater.inflate(R.layout.weather_view, container, false);
        address = view.findViewById(R.id.address);
        address.setText(curLocation.getStreetAddress());
        initAdMob();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("WeatherView", "onViewCreated");
    }

    public void getLocationData() {
        LocationDatabase locationDB = new LocationDatabase(getContext());
        curLocation = locationDB.readFirstLocation();

        if(curLocation != null) {

        }
    }

    public void getWeatherData() {

    }

    public void initAdMob() {
        MobileAds.initialize(getContext(), initializationStatus -> { });

    }
}
