package com.heon9u.alarm_weather_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.heon9u.alarm_weather_app.R;

public class MainActivity extends AppCompatActivity {
    protected final String openweatherUrl = "https://api.openweathermap.org/data/2.5/onecall";
    protected final String apiKey = "6e20ff161911d310524f6a26ac649500";

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private AlarmListView alarmListView = new AlarmListView();
    private WeatherView weatherView = new WeatherView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, alarmListView).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId()) {
                case R.id.alarmItem:
                    System.out.println("alarm");
                    transaction.replace(R.id.frameLayout, alarmListView).commitAllowingStateLoss();
                    break;
                case R.id.weatherItem:
                    System.out.println("weather");
                    transaction.replace(R.id.frameLayout, weatherView).commitAllowingStateLoss();
                    break;
            }

            return true;
        }
    }

    public void searchHourlyForecast() {
        String lat = "37.45746122172504";
        String lon = "126.72263584810149";

        String hourlyUrl = openweatherUrl + "?lat=" + lat + "&lon=" + lon +
                "&appid=" + apiKey + "&units=metric" + "&lang=kr";

        HourlyForecast hourlyForecast = new HourlyForecast();
        hourlyForecast.execute(hourlyUrl);
    }
}