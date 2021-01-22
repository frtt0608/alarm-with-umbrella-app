package com.heon9u.alarm_weather_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.R;

import java.util.Calendar;

public class AlarmOnActivity extends AppCompatActivity {

    TextView time;
    Button stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("AlarmOnActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_on);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Calendar calendar = Calendar.getInstance();

        time = findViewById(R.id.time);
        time.setText(calendar.getTime().toString());

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(view -> {
            Intent serviceIntent = new Intent(this, AlarmService.class);
            stopService(serviceIntent);
            Log.d("AlarmOnActivity", "알람 해제하기");
            finish();
        });
    }
}
