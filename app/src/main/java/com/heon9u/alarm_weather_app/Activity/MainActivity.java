package com.heon9u.alarm_weather_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.heon9u.alarm_weather_app.R;

public class MainActivity extends AppCompatActivity {
    protected final String openweatherUrl = "https://api.openweathermap.org/data/2.5/onecall";
    protected final String apiKey = "6e20ff161911d310524f6a26ac649500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String lat = "37.45746122172504";
        String lon = "126.72263584810149";

        String hourlyUrl = openweatherUrl + "?lat=" + lat + "&lon=" + lon +
                            "&appid=" + apiKey + "&units=metric" + "&lang=kr";

        HourlyForecast hourlyForecast = new HourlyForecast();
        hourlyForecast.execute(hourlyUrl);
    }
}