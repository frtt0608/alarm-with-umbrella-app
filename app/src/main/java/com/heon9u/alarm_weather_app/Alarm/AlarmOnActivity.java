package com.heon9u.alarm_weather_app.Alarm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.Dto.CurrentWeather;
import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.Dto.Weather;
import com.heon9u.alarm_weather_app.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlarmOnActivity extends AppCompatActivity implements View.OnClickListener {

    TextView address, temp, day, time;
    ImageView weatherImage;
    Button stop;
    Location location;
    CurrentWeather currentWeather;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("AlarmOnActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_on);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setView();

        Intent preIntent = getIntent();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop:
                stopAlarm();
                break;
        }
    }

    public void setView() {
        address = findViewById(R.id.address);
        weatherImage = findViewById(R.id.weatherImage);
        temp = findViewById(R.id.temp);
        day = findViewById(R.id.day);
        time = findViewById(R.id.time);

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(this);
    }

    public void stopAlarm() {
        Intent serviceIntent = new Intent(this, AlarmService.class);
        stopService(serviceIntent);
        Log.d("AlarmOnActivity", "알람 해제하기");
        finish();
    }

    public void setLocationView() {
        String addressStr = location.getStreetAddress();
        if(addressStr == null)
            addressStr = location.getLotAddress();

        address.setText(addressStr);
    }

    public void setWeatherView() {
        Weather weather = currentWeather.getWeather();
        String[] state = {"", "", "thunderstorm", "drizzle",
                            "", "rain", "snow", "mist", "clear"};
        int id = weather.getId();

        if(id/100 < 8 || id == 800) {
            int drawableId = getResources().getIdentifier(state[id/100], "drawable", getPackageName());
            Log.d("날씨상태", drawableId+"");
            weatherImage.setImageResource(drawableId);
        } else {
            weatherImage.setImageResource(R.drawable.clouds);
        }

        temp.setText(Double.toString(currentWeather.getTemp()) + "°C");
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
        day.setVisibility(View.INVISIBLE);
        time.setVisibility(View.INVISIBLE);
    }
}
