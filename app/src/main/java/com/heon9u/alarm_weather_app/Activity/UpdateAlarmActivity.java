package com.heon9u.alarm_weather_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.R;

public class UpdateAlarmActivity extends AppCompatActivity {

    Alarm alarm;
    EditText title;
    TimePicker timePicker;
    Button update_button, cancel_button;
    String alarmTitle;
    int alarmHour, alarmMinute;

    public void getTargetAlarm() {
        Intent preIntent = getIntent();

        this.alarm = (Alarm) preIntent.getSerializableExtra("Alarm");
        alarmHour = alarm.getHour();
        alarmMinute = alarm.getMinute();
        alarmTitle = alarm.getTitle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_update);

        getTargetAlarm();
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new UpdateAlarmActivity.timeChangedListener());
        title = findViewById(R.id.title);

        timePicker.setCurrentHour(alarmHour);
        timePicker.setCurrentMinute(alarmMinute);
        title.setText(alarmTitle);

        update_button = findViewById(R.id.update_button);
        cancel_button = findViewById(R.id.cancel_button);

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabaseHelper appDB = new AppDatabaseHelper(UpdateAlarmActivity.this);

                alarmTitle = title.getText().toString();
                appDB.updateAlarm(alarm.getId(), alarmHour, alarmMinute, alarmTitle);

                backToAlarmListView();
                finish();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToAlarmListView();
                finish();
            }
        });
    }

    public void backToAlarmListView() {
        Intent main = new Intent(UpdateAlarmActivity.this, MainActivity.class);
        startActivity(main);
    }

    public class timeChangedListener implements TimePicker.OnTimeChangedListener {

        @Override
        public void onTimeChanged(TimePicker timeView, int hour, int minute) {
            alarmHour = hour;
            alarmMinute = minute;
        }
    }
}
