package com.heon9u.alarm_weather_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.R;

public class CreateAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    TimePicker timePicker;
    int alarmHour, alarmMinute;
    EditText title;
    Switch allDaySwitch, allSoundSwitch, basicSoundSwitch, umbSoundSwitch, vibSwitch;
    TextView basicSound, umbSound;
    Button[] dayButton = new Button[7];
    Button create_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_create);

        setTimePicker();
        setButtonView();
        setTextView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_button:
                AppDatabaseHelper appDB = new AppDatabaseHelper(CreateAlarmActivity.this);

                String alarmTitle = title.getText().toString();
                appDB.createAlarm(alarmHour, alarmMinute, alarmTitle);

            case R.id.cancel_button:
                backToAlarmListView();
                finish();

            case R.id.sun:

        }
    }

    public void setTimePicker() {
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        alarmHour = timePicker.getCurrentHour();
        alarmMinute = timePicker.getCurrentMinute();
        timePicker.setOnTimeChangedListener(new timeChangedListener());
    }

    public void setTextView() {
        title = findViewById(R.id.title);
        basicSound = findViewById(R.id.basicSound);
        umbSound = findViewById(R.id.umbSound);
    }

    public void setButtonView() {
        allDaySwitch = findViewById(R.id.allDaySwitch);
        dayButton[0] = findViewById(R.id.sun);
        dayButton[1] = findViewById(R.id.mon);
        dayButton[2] = findViewById(R.id.tue);
        dayButton[3] = findViewById(R.id.wen);
        dayButton[4] = findViewById(R.id.thu);
        dayButton[5] = findViewById(R.id.fri);
        dayButton[6] = findViewById(R.id.sat);
        allSoundSwitch = findViewById(R.id.allSoundSwitch);

        basicSoundSwitch = findViewById(R.id.basicSoundSwitch);
        umbSoundSwitch = findViewById(R.id.umbSoundSwitch);
        vibSwitch = findViewById(R.id.vibSwitch);

        create_button = findViewById(R.id.create_button);
        cancel_button = findViewById(R.id.cancel_button);
    }


    public class timeChangedListener implements TimePicker.OnTimeChangedListener {

        @Override
        public void onTimeChanged(TimePicker timeView, int hour, int minute) {
            alarmHour = hour;
            alarmMinute = minute;
        }
    }

    public void backToAlarmListView() {
        Intent main = new Intent(CreateAlarmActivity.this, MainActivity.class);
        startActivity(main);
    }
}
