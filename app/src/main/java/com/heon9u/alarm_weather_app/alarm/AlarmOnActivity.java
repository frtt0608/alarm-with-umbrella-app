package com.heon9u.alarm_weather_app.alarm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.ebanx.swipebtn.SwipeButton;
import com.heon9u.alarm_weather_app.alarm.database.AlarmViewModel;
import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.dto.CurrentWeather;
import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.dto.Weather;
import com.heon9u.alarm_weather_app.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AlarmOnActivity extends AppCompatActivity {

    TextView address, temp, windChillTemp, day, time;
    ImageView weatherImage;
    SwipeButton stop;
    Alarm alarm;
    Location location;
    CurrentWeather currentWeather;
    ConstraintLayout backLayout, locationLayout;
    int deviceHeight, deviceWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_on);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setView();

        Intent preIntent = getIntent();
        alarm = (Alarm) preIntent.getSerializableExtra("alarm");
        Serializable serializable = preIntent.getSerializableExtra("location");

        if(serializable != null) {
            location = (Location) serializable;
            currentWeather = (CurrentWeather) preIntent.getSerializableExtra("weather");
            setLocationView();
            setWeatherView();
        } else {
            setInvisibleView();
        }

        setTimeView();
    }

    public void getDisplaySize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int density = (int) getResources().getDisplayMetrics().density;

        deviceHeight = outMetrics.heightPixels;
        deviceWidth = outMetrics.widthPixels;
    }

    public void applyDeviceSize() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) locationLayout.getLayoutParams();
        layoutParams.topMargin = deviceHeight/15;

        layoutParams = (ConstraintLayout.LayoutParams) stop.getLayoutParams();
        layoutParams.leftMargin = deviceWidth/5;
        layoutParams.rightMargin = deviceWidth/5;
    }

    public void setView() {
        backLayout = findViewById(R.id.backLayout);
        locationLayout = findViewById(R.id.locationLayout);
        address = findViewById(R.id.address);
        weatherImage = findViewById(R.id.weatherImage);
        temp = findViewById(R.id.temp);
        windChillTemp = findViewById(R.id.windChillTemp);
        day = findViewById(R.id.day);
        time = findViewById(R.id.time);
        stop = findViewById(R.id.stop);
        stop.setOnStateChangeListener(active -> {
            changeAlarmTotalFlag();
            stopAlarmAndFinishApp();
        });

        getDisplaySize();
        applyDeviceSize();
    }

    public void changeAlarmTotalFlag() {
        if (alarm.getDay().equals("")) {
            AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
            alarm.setTotalFlag(false);
            alarmViewModel.update(alarm);
        } else {
            setRepeatAlarm();
        }
    }

    public void setRepeatAlarm() {
        Intent alarmIntent = new Intent(this, AlarmManagerActivity.class);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("request", "create");
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(alarmIntent);
    }

    public void stopAlarmAndFinishApp() {
        Intent serviceIntent = new Intent(this, AlarmService.class);
        stopService(serviceIntent);

        moveTaskToBack(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { finishAndRemoveTask(); }
        else { finish(); }
    }

    public void setLocationView() {
        String addressStr = location.getStreetAddress();
        if(addressStr == null)
            addressStr = location.getLotAddress();

        address.setText(addressStr);
    }

    public void setWeatherView() {
        Weather weather = currentWeather.getWeather();
        String[] weatherState = {"", "", "weather_thunderstorm", "weather_drizzle",
                            "", "weather_rain", "weather_snow", "weather_mist", "weather_clear"};
        String[] backgroundState = {"", "", "back_thunderstorm", "back_rain",
                            "", "back_rain", "back_snow", "back_mist", "back_clear"};

        int id = weather.getId();

        if(id/100 < 8 || id == 800) {
            int weatherId = getResources().getIdentifier(weatherState[id/100],
                                                    "drawable", getPackageName());
            int backgroundId = getResources().getIdentifier(backgroundState[id/100],
                                                            "drawable", getPackageName());

            weatherImage.setImageResource(weatherId);
            backLayout.setBackgroundResource(backgroundId);
        } else {
            weatherImage.setImageResource(R.drawable.weather_clouds);
            backLayout.setBackgroundResource(R.drawable.back_clouds);
        }

        temp.setText(currentWeather.getTemp() + "°C");
        windChillTemp.setText("(체감온도" + currentWeather.getFeels_like() + "°C)");
    }

    public void setTimeView() {
        String[] timeArr = changeUTCtoDate().split(" ");
        String dayStr = timeArr[0] + " " + timeArr[1] + " (" + timeArr[2].charAt(0) + ")";
        String timeStr = timeArr[3];

        day.setText(dayStr);
        time.setText(timeStr);
    }

    // UTC를 date로 변환
    // 2021-01-04 05:00:00
    public String changeUTCtoDate() {
        long dt = System.currentTimeMillis();
        Date date = new Date(dt);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM월 dd일 EE요일 HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));

        return sdf.format(date);
    }

    public void setInvisibleView() {
        address.setVisibility(View.INVISIBLE);
        weatherImage.setVisibility(View.INVISIBLE);
        temp.setVisibility(View.INVISIBLE);
        windChillTemp.setVisibility(View.INVISIBLE);
    }

    // 볼륨 조절 제한.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
