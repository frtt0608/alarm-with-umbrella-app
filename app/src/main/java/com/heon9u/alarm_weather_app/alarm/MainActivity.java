package com.heon9u.alarm_weather_app.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.heon9u.alarm_weather_app.openweather.WeatherView;
import com.heon9u.alarm_weather_app.R;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private AlarmListView alarmListView = new AlarmListView();
    private WeatherView weatherView = new WeatherView();
    private FragmentTransaction transaction;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, alarmListView).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId()) {
                case R.id.alarmItem:
                    transaction.replace(R.id.frameLayout, alarmListView).commitAllowingStateLoss();
                    break;
//                case R.id.weatherItem:
//                    transaction.replace(R.id.frameLayout, weatherView).commitAllowingStateLoss();
//                    break;
            }

            return true;
        }
    }

    public void checkPermission() {
        checkOverLaysPermission();
        checkIgnoringBatteryOptimization();
    }

    public void checkOverLaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {// 체크
                showDialogForOverLays();
            }
        }
    }

    public void checkIgnoringBatteryOptimization() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    public void showDialogForOverLays() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알람 해제 창이 안보일 수 있어요!")
                .setMessage("알람이 울릴 때, 화면 위로 알람 해제 창이 보이도록" +
                        "다른 앱 위에 표시 권한'을 허용해주세요.")
                .setNeutralButton("설정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setOverLaysPermission();
                    }
                })
                .setCancelable(false);
        builder.show();
    }

    public void setOverLaysPermission() {
        Uri uri = Uri.parse("package:" + getPackageName());
        Intent callOverLaysPermissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
        startActivity(callOverLaysPermissionIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return false;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}