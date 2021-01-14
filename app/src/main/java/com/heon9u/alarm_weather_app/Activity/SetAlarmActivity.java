package com.heon9u.alarm_weather_app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.R;

public class SetAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    Intent preIntent, alarmIntent;

    int dayTrue, dayFalse;
    String day;
    int alarmHour, alarmMinute;
    Alarm newAlarm, updateAlarm;

    TimePicker timePicker;
    EditText title;
    Switch allDaySwitch, basicSoundSwitch, umbSoundSwitch, vibSwitch;
    boolean allDayFlag, basicSoundFlag, umbSoundFlag, vibFlag;
    TextView basicSound, umbSound;
    Button[] dayButton = new Button[8];
    boolean[] dayArr = new boolean[8];
    Button saveButton, cancelButton;

    ConstraintLayout basicSoundLayout, umbSoundLayout, vibLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set);

        dayTrue = getResources().getColor(R.color.purple_200);
        dayFalse = getResources().getColor(R.color.light_grey);

        setTimePicker();
        setObjectView();

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        for(int i=1; i<dayButton.length; i++) {
            dayButton[i].setOnClickListener(this);
        }

        allDaySwitch.setOnCheckedChangeListener(new switchListener());
        basicSoundLayout.setOnClickListener(this);
        umbSoundLayout.setOnClickListener(this);
        vibLayout.setOnClickListener(this);

        //
        preIntent = getIntent();
        updateAlarm = (Alarm) preIntent.getSerializableExtra("alarm");
        if(updateAlarm != null) {
            setAlarmView(updateAlarm.getId());
        }
    }

    public void setAlarmView(int id) {
        AppDatabaseHelper appDB = new AppDatabaseHelper(SetAlarmActivity.this);
        Cursor cursor = appDB.readAlarm(id);

        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No alarm", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToNext();

            timePicker.setCurrentHour(cursor.getInt(1));
            timePicker.setCurrentMinute(cursor.getInt(2));
            title.setText(cursor.getString(3));
            allDayFlag = cursor.getInt(5) > 0;
            allDaySwitch.setChecked(allDayFlag);
            if(!allDayFlag)
                setDayColumn(cursor);

            basicSoundSwitch.setChecked(cursor.getInt(7) > 0);
            basicSound.setText(cursor.getString(8));
            umbSoundSwitch.setChecked(cursor.getInt(9) > 0);
            umbSound.setText(cursor.getString(10));
            vibSwitch.setChecked(cursor.getInt(11) > 0);
        }
    }

    public void setDayColumn(Cursor cursor) {
        day = cursor.getString(6);
        if(!day.equals("")) {
            String[] daySplit = day.split(",");
            for(int i=0; i<daySplit.length; i++) {
                clickDayButton(Integer.parseInt(daySplit[i]));
            }
        }
    }

    public void clickDayButton(int i) {
        dayButton[i].setBackgroundColor(dayArr[i] ? dayFalse : dayTrue);
        dayArr[i] = !dayArr[i];
    }

    @Override
    public void onClick(View v) {
        int i = 0;

        switch (v.getId()) {
            case R.id.saveButton:
                AppDatabaseHelper appDB = new AppDatabaseHelper(SetAlarmActivity.this);
                setAlarm();
                if(updateAlarm == null) {
                    appDB.setDatabaseAlarm(newAlarm, "create");
                } else {
                    newAlarm.setId(updateAlarm.getId());
                    appDB.setDatabaseAlarm(newAlarm, "update");
                }

                alarmIntent = new Intent(this, AlarmActivity.class);
                alarmIntent.putExtra("alarm", newAlarm);
                alarmIntent.putExtra("request", "create");
                startActivity(alarmIntent);

            case R.id.cancelButton:
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
                clickDayButton(1);
                break;

            case R.id.mon:
                clickDayButton(2);
                break;

            case R.id.tue:
                clickDayButton(3);
                break;

            case R.id.wen:
                clickDayButton(4);
                break;

            case R.id.thu:
                clickDayButton(5);
                break;

            case R.id.fri:
                clickDayButton(6);
                break;

            case R.id.sat:
                clickDayButton(7);
                break;
        }
    }

    public void setAlarm() {
        newAlarm = new Alarm();
        newAlarm.setHour(alarmHour);
        newAlarm.setMinute(alarmMinute);
        newAlarm.setTitle(title.getText().toString());
        newAlarm.setTotalFlag(true);
        newAlarm.setAllDayFlag(allDayFlag);

        day = "";
        for(int i=1; i<dayArr.length; i++) {
            if(dayArr[i]) {
                day += i + ",";
            }
        }
        if(day.length() > 0) {
            day = day.substring(0, day.length()-1);
        }

        newAlarm.setDay(day);
        newAlarm.setBasicSoundFlag(basicSoundFlag);
        newAlarm.setBasicSound(basicSound.getText().toString());
        newAlarm.setUmbSoundFlag(umbSoundFlag);
        newAlarm.setUmbSound(umbSound.getText().toString());
        newAlarm.setVibFlag(vibFlag);
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
        dayButton[1] = findViewById(R.id.sun);
        dayButton[2] = findViewById(R.id.mon);
        dayButton[3] = findViewById(R.id.tue);
        dayButton[4] = findViewById(R.id.wen);
        dayButton[5] = findViewById(R.id.thu);
        dayButton[6] = findViewById(R.id.fri);
        dayButton[7] = findViewById(R.id.sat);

        basicSoundSwitch = findViewById(R.id.basicSoundSwitch);
        umbSoundSwitch = findViewById(R.id.umbSoundSwitch);
        vibSwitch = findViewById(R.id.vibSwitch);

        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

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
                for(int i=1; i<dayButton.length; i++) {
                    dayButton[i].setBackgroundColor(dayTrue);
                    allDayFlag = true;
                    dayArr[i] = true;
                }
            } else {
                for(int i=1; i<dayButton.length; i++) {
                    dayButton[i].setBackgroundColor(dayFalse);
                    allDayFlag = false;
                    dayArr[i] = false;
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
        Intent main = new Intent(SetAlarmActivity.this, MainActivity.class);
        startActivity(main);
    }

    @Override
    public void onBackPressed() {
        backToAlarmListView();
        finish();
    }
}
