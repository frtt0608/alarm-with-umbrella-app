package com.heon9u.alarm_weather_app.Alarm;

import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.Location.LocationDatabase;
import com.heon9u.alarm_weather_app.Location.LocationListView;
import com.heon9u.alarm_weather_app.R;

public class AlarmSetActivity extends AppCompatActivity implements View.OnClickListener {

    final int REQUEST_CODE_BASIC_SOUND = 1000;
    final int REQUEST_CODE_UMB_SOUND = 1001;
    final int REQUEST_CODE_LOCATION = 100;

    Intent preIntent;
    int dayTrue, dayFalse;
    String REQUEST_STATE, day, basicSoundStr, umbSoundStr;
    int alarmHour, alarmMinute, alarmVolume, location_id;
    Alarm newAlarm, updateAlarm;
    Location location;

    TimePicker timePicker;
    EditText title;
    Switch allDaySwitch, basicSoundSwitch, umbSoundSwitch, vibSwitch;
    boolean allDayFlag, basicSoundFlag, umbSoundFlag, vibFlag;
    TextView basicSound, umbSound, currentAddress;
    Button[] dayButton = new Button[8];
    boolean[] dayArr = new boolean[8];
    Button saveButton, cancelButton;
    SeekBar volume;

    ConstraintLayout basicSoundLayout, umbSoundLayout, vibLayout, locationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set);

        dayTrue = getResources().getColor(R.color.purple_200);
        dayFalse = getResources().getColor(R.color.light_grey);

        setTimePicker();
        setObjectView();
        setVolumeChanged();

        basicSoundStr = "content://settings/system/ringtone";
        umbSoundStr = "content://settings/system/ringtone";

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        for(int i=1; i<dayButton.length; i++) {
            dayButton[i].setOnClickListener(this);
        }

        allDaySwitch.setOnCheckedChangeListener(new switchListener());
        basicSoundSwitch.setOnCheckedChangeListener(new switchListener());
        umbSoundSwitch.setOnCheckedChangeListener(new switchListener());
        vibSwitch.setOnCheckedChangeListener(new switchListener());
        basicSoundLayout.setOnClickListener(this);
        umbSoundLayout.setOnClickListener(this);
        locationLayout.setOnClickListener(this);

        preIntent = getIntent();
        REQUEST_STATE = preIntent.getStringExtra("REQUEST_STATE");

        if(REQUEST_STATE.equals("update")) {
            updateAlarm = (Alarm) preIntent.getSerializableExtra("alarm");
            location_id = updateAlarm.getLocation_id();
            readLocation();
            setAlarmView(updateAlarm.getId());
        }
    }

    public void readLocation() {
        LocationDatabase locationDB = new LocationDatabase(AlarmSetActivity.this);
        Cursor cursor = locationDB.readLocation(location_id);

        if(cursor.getCount() == 0) {
            Log.d("AlarmSetActivity", "no location_id data");
        } else {
            cursor.moveToNext();

            location = new Location();
            location.setId(cursor.getInt(0));
            location.setStreetAddress(cursor.getString(1));
            location.setLotAddress(cursor.getString(2));
            location.setCommunityCenter(cursor.getString(3));
            location.setLatitude(cursor.getDouble(4));
            location.setLongitude(cursor.getDouble(5));
        }
    }

    public void setAlarmView(int id) {
        AlarmDatabase alarmDB = new AlarmDatabase(AlarmSetActivity.this);
        Cursor cursor = alarmDB.readAlarm(id);

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
            volume.setProgress(cursor.getInt(7));
            basicSoundSwitch.setChecked(cursor.getInt(8) > 0);
            basicSoundStr = cursor.getString(9);
            basicSound.setText(decodingUri(basicSoundStr));
            umbSoundSwitch.setChecked(cursor.getInt(10) > 0);
            umbSoundStr = cursor.getString(11);
            umbSound.setText(decodingUri(umbSoundStr));
            vibSwitch.setChecked(cursor.getInt(12) > 0);

            String address = location.getStreetAddress();
            if(address == null) address = location.getLotAddress();
            currentAddress.setText(address);
        }

        alarmDB.close();
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

    public void setVolumeChanged() {
        alarmVolume = 30;
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alarmVolume = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void registeredAlarmManager(String request) {
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmManagerActivity.class);
        alarmIntent.putExtra("alarm", newAlarm);
        alarmIntent.putExtra("request", request);
        startActivity(alarmIntent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.saveButton:
                AlarmDatabase alarmDB = new AlarmDatabase(AlarmSetActivity.this);
                setAlarm();

                if(REQUEST_STATE.equals("create")) {
                    alarmDB.setDatabaseAlarm(newAlarm, "create");
                } else {
                    newAlarm.setId(updateAlarm.getId());
                    alarmDB.setDatabaseAlarm(newAlarm, "update");
                }
                alarmDB.close();
                registeredAlarmManager("create");

            case R.id.cancelButton:
                finish();
                break;

            case R.id.basicSoundLayout:
                setRingtone("basic");
                break;

            case R.id.umbSoundLayout:
                setRingtone("umb");
                break;

            case R.id.locationLayout:
                setLocation();
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
        newAlarm.setVolume(alarmVolume);
        newAlarm.setBasicSoundFlag(basicSoundFlag);
        newAlarm.setBasicSound(basicSoundStr);
        newAlarm.setUmbSoundFlag(umbSoundFlag);
        newAlarm.setUmbSound(umbSoundStr);
        newAlarm.setVibFlag(vibFlag);

        if(location == null) {
            newAlarm.setLocation_id(0);
        } else {
            newAlarm.setLocation_id(location.getId());
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
        currentAddress = findViewById(R.id.currentAddress);

        allDaySwitch = findViewById(R.id.allDaySwitch);
        dayButton[1] = findViewById(R.id.sun);
        dayButton[2] = findViewById(R.id.mon);
        dayButton[3] = findViewById(R.id.tue);
        dayButton[4] = findViewById(R.id.wen);
        dayButton[5] = findViewById(R.id.thu);
        dayButton[6] = findViewById(R.id.fri);
        dayButton[7] = findViewById(R.id.sat);

        volume = findViewById(R.id.volume);
        basicSoundSwitch = findViewById(R.id.basicSoundSwitch);
        umbSoundSwitch = findViewById(R.id.umbSoundSwitch);
        vibSwitch = findViewById(R.id.vibSwitch);

        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        basicSoundLayout = findViewById(R.id.basicSoundLayout);
        umbSoundLayout = findViewById(R.id.umbSoundLayout);
        vibLayout = findViewById(R.id.vibLayout);
        locationLayout = findViewById(R.id.locationLayout);
    }

    public class switchListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.allDaySwitch:
                    daySwitch(isChecked);
                    break;
                case R.id.basicSoundSwitch:
                    basicSoundFlag = isChecked;
                    break;
                case R.id.umbSoundSwitch:
                    umbSoundFlag = isChecked;
                    break;
                case R.id.vibSwitch:
                    vibFlag = isChecked;
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

    public void setLocation() {
        Intent locationIntent = new Intent(this, LocationListView.class);
        this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION);
    }

    public void setRingtone(String type) {
        Intent ringIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        ringIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "알람음을 선택하세요!");
        ringIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        ringIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        ringIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);

        if(type.equals("basic"))
            this.startActivityForResult(ringIntent, REQUEST_CODE_BASIC_SOUND);
        else
            this.startActivityForResult(ringIntent, REQUEST_CODE_UMB_SOUND);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode >= 1000) {
            if(resultCode == RESULT_OK) {
                Uri choiceRingtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Log.d("AlarmSetActivity", choiceRingtone.toString());
                // content://settings/system/ringtone
                switch (requestCode) {
                    case REQUEST_CODE_BASIC_SOUND:
                        if (choiceRingtone != null) {
                            basicSoundStr = choiceRingtone.toString();
                            basicSound.setText(decodingUri(basicSoundStr));
                        }
                        break;
                    case REQUEST_CODE_UMB_SOUND:
                        if (choiceRingtone != null) {
                            umbSoundStr = choiceRingtone.toString();
                            umbSound.setText(decodingUri(umbSoundStr));
                        }
                        break;
                }
            }
        } else if(requestCode >= 100) {
            if(resultCode == RESULT_OK) {
                Log.d("AlarmSetActivity", "위치 받기");

                Location choiceLocation = (Location) data.getSerializableExtra("location");
                if(choiceLocation != null) {
                    location = choiceLocation;

                    String address = location.getStreetAddress();
                    if(address == null) address = location.getLotAddress();
                    currentAddress.setText(address);
                }
            }
        }
    }

    public String decodingUri(String uri) {
        String uriStr = Uri.decode(uri);
        int s = uriStr.indexOf("=") + 1;
        int e = uriStr.indexOf("&");

        if(s == 0 || e == 0) return "기본음"; // 기본음
        return uriStr.substring(s, e);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
