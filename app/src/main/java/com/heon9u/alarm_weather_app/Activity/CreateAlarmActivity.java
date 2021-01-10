package com.heon9u.alarm_weather_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.R;

public class CreateAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    int dayTrue, dayFalse;
    int alarmHour, alarmMinute;

    TimePicker timePicker;
    EditText title;
    Switch allDaySwitch, allSoundSwitch, basicSoundSwitch, umbSoundSwitch, vibSwitch;
    TextView basicSound, umbSound;
    Button[] dayButton = new Button[7];
    boolean[] day = new boolean[7];
    Button create_button, cancel_button;

    ConstraintLayout basicSoundLayout, umbSoundLayout, vibLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_create);

        dayTrue = getResources().getColor(R.color.purple_200);
        dayFalse = getResources().getColor(R.color.light_grey);

        setTimePicker();
        setObjectView();

        create_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
        for(int i=0; i<dayButton.length; i++) {
            dayButton[i].setOnClickListener(this);
        }

        allDaySwitch.setOnCheckedChangeListener(new switchListener());
        basicSoundLayout.setOnClickListener(this);
        umbSoundLayout.setOnClickListener(this);
        vibLayout.setOnClickListener(this);
    }

    public void clickDayButton(int i) {
        dayButton[i].setBackgroundColor(day[i] ? dayFalse : dayTrue);
        day[i] = !day[i];
    }

    @Override
    public void onClick(View v) {
        int i = 0;

        switch (v.getId()) {
            case R.id.create_button:
                AppDatabaseHelper appDB = new AppDatabaseHelper(CreateAlarmActivity.this);
                String alarmTitle = title.getText().toString();
                appDB.createAlarm(alarmHour, alarmMinute, alarmTitle);

            case R.id.cancel_button:
                backToAlarmListView();
                finish();
                break;

            case R.id.basicSoundLayout:
                Toast.makeText(this, "touch basicSoundLayout", Toast.LENGTH_SHORT).show();
                break;

            case R.id.umbSoundLayout:
                Toast.makeText(this, "touch umbSoundLayout", Toast.LENGTH_SHORT).show();
                break;

            case R.id.vibLayout:
                Toast.makeText(this, "touch vibLayout", Toast.LENGTH_SHORT).show();
                break;

            case R.id.sun:
                clickDayButton(0);
                break;

            case R.id.mon:
                clickDayButton(1);
                break;

            case R.id.tue:
                clickDayButton(2);
                break;

            case R.id.wen:
                clickDayButton(3);
                break;

            case R.id.thu:
                clickDayButton(4);
                break;

            case R.id.fri:
                clickDayButton(5);
                break;

            case R.id.sat:
                clickDayButton(6);
                break;
        }
    }

    public void setTimePicker() {
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        alarmHour = timePicker.getCurrentHour();
        alarmMinute = timePicker.getCurrentMinute();
        timePicker.setOnTimeChangedListener(new timeChangedListener());
    }

    public void setObjectView() {
        title = findViewById(R.id.title);
        basicSound = findViewById(R.id.basicSound);
        umbSound = findViewById(R.id.umbSound);

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

        basicSoundLayout = findViewById(R.id.basicSoundLayout);
        umbSoundLayout = findViewById(R.id.umbSoundLayout);
        vibLayout = findViewById(R.id.vibLayout);
    }

    public class switchListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.allDaySwitch:
                    daySwitch(isChecked);
                    break;
            }
        }

        public void daySwitch(boolean isChecked) {
            if(isChecked) {
                for(int i=0; i<dayButton.length; i++) {
                    dayButton[i].setBackgroundColor(dayTrue);
                    day[i] = true;
                }
            } else {
                for(int i=0; i<dayButton.length; i++) {
                    dayButton[i].setBackgroundColor(dayFalse);
                    day[i] = false;
                }
            }
        }
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

    @Override
    public void onBackPressed() {
        backToAlarmListView();
        finish();
    }
}
