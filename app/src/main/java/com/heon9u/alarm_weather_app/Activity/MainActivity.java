package com.heon9u.alarm_weather_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
        permissionCheck();

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
                    transaction.replace(R.id.frameLayout, alarmListView).commitAllowingStateLoss();
                    break;
                case R.id.weatherItem:
                    transaction.replace(R.id.frameLayout, weatherView).commitAllowingStateLoss();
                    break;
            }

            return true;
        }
    }

    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {// 체크
                onAlertDialog();
            }
        }
    }

    public void onAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알람 해제 창이 안보일 수 있어요!")
                .setMessage("알람이 울릴 때, 화면 위로 알람 해제 창이 보이도록" +
                        "다른 앱 위에 표시 권한'을 허용해주세요.")
                .setNeutralButton("설정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingPermission();
                    }
                })
                .setCancelable(false);
        builder.show();
    }

    public void settingPermission() {
        Uri uri = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                uri);
        startActivity(intent);
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