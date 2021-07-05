package com.heon9u.alarm_weather_app.alarm;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.dto.AlarmBuilder;
import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.dto.LocationBuilder;
import com.heon9u.alarm_weather_app.dto.Ringtone;
import com.heon9u.alarm_weather_app.location.LocationListView;
import com.heon9u.alarm_weather_app.R;
import com.heon9u.alarm_weather_app.location.database.LocationDao;
import com.heon9u.alarm_weather_app.location.database.LocationDatabase;
import com.heon9u.alarm_weather_app.location.database.LocationRepository;
import com.heon9u.alarm_weather_app.location.database.LocationViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlarmSetActivity extends AppCompatActivity implements View.OnClickListener {

    final int REQUEST_CODE_BASIC_SOUND = 1000;
    final int REQUEST_CODE_UMB_SOUND = 1001;
    final int REQUEST_CODE_LOCATION = 100;

    Intent preIntent;
    Alarm newAlarm, updateAlarm;
    Location location;

    int dayTrue, dayFalse;
    String REQUEST_STATE, day, basicSoundUri, umbSoundUri;
    int alarmHour, alarmMinute, alarmVolume;
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
        newAlarm = new Alarm();

        dayTrue = getResources().getColor(R.color.purple_200);
        dayFalse = getResources().getColor(R.color.light_grey);
        alarmVolume = 100;
        basicSoundUri = "content://settings/system/ringtone";
        umbSoundUri = "content://settings/system/ringtone";

        setTimePicker();
        setObjectView();
        setVolumeChanged();

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        for (int i = 1; i < dayButton.length; i++) {
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

        if (REQUEST_STATE.equals("update")) {
            updateAlarm = (Alarm) preIntent.getSerializableExtra("alarm");
            if (updateAlarm.getLocation_id() != -1) {
                readLocation();
            }

            setAlarmView();
        }
    }

    public void readLocation() {
        LocationViewModel locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel.getLocation(updateAlarm.getLocation_id())
                .subscribe(locationItem -> {
                    this.location = locationItem;

                    if (location != null) {
                        String address = location.getStreetAddress();
                        if (address == null) address = location.getLotAddress();
                        currentAddress.setText(address);
                    }
                });
    }

    public void setAlarmView() {
        timePicker.setCurrentHour(updateAlarm.getHour());
        timePicker.setCurrentMinute(updateAlarm.getMinute());
        title.setText(updateAlarm.getTitle());
        allDayFlag = updateAlarm.isAllDayFlag();
        allDaySwitch.setChecked(allDayFlag);

        if (!allDayFlag) {
            setDayColumn();
        }

        volume.setProgress(updateAlarm.getVolume());
        basicSoundSwitch.setChecked(updateAlarm.isBasicSoundFlag());
        basicSound.setText(updateAlarm.getBasicSoundTitle());
        umbSoundSwitch.setChecked(updateAlarm.isUmbSoundFlag());
        umbSound.setText(updateAlarm.getUmbSoundTitle());
        vibSwitch.setChecked(updateAlarm.isVibFlag());
    }

    public void setAlarm() {
        day = setDayString();

        if (location == null) {
            location = new LocationBuilder().build();
        }

        newAlarm = new AlarmBuilder()
                .setHour(alarmHour)
                .setMinute(alarmMinute)
                .setTitle(title.getText().toString())
                .setAllDayFlag(allDayFlag)
                .setDay(day)
                .setVolume(alarmVolume)
                .setBasicSoundFlag(basicSoundFlag)
                .setBasicSoundTitle(basicSound.getText().toString())
                .setBasicSoundUri(basicSoundUri)
                .setUmbSoundFlag(umbSoundFlag)
                .setUmbSoundTitle(umbSound.getText().toString())
                .setUmbSoundUri(umbSoundUri)
                .setVibFlag(vibFlag)
                .setLocation_id(location.getId())
                .build();
    }

    public void setDayColumn() {
        day = updateAlarm.getDay();

        if (!day.equals("")) {
            String[] daySplit = day.split(",");
            for (int i = 0; i < daySplit.length; i++) {
                clickDayButton(Integer.parseInt(daySplit[i]));
            }

            if (daySplit.length == 7) {
                allDaySwitch.setChecked(true);
                allDayFlag = true;
            }
        }
    }

    public String setDayString() {
        String day = "";
        for (int i = 1; i < dayArr.length; i++) {
            if (dayArr[i]) {
                day += i + ",";
            }
        }

        if (day.length() > 0) {
            day = day.substring(0, day.length() - 1);
        }

        return day;
    }

    public void clickDayButton(int i) {
        dayButton[i].setBackgroundColor(dayArr[i] ? dayFalse : dayTrue);
        dayArr[i] = !dayArr[i];

        for (int j = 1; j < dayArr.length; j++) {
            if (!dayArr[j]) {
                allDaySwitch.setChecked(false);
                allDayFlag = false;
                return;
            }
        }

        allDaySwitch.setChecked(true);
        allDayFlag = true;
    }

    public void setVolumeChanged() {
        alarmVolume = 100;

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.saveButton:
                setAlarm();
                if (REQUEST_STATE.equals("update")) {
                    newAlarm.setId(updateAlarm.getId());
                }

                Intent data = new Intent();
                data.putExtra("alarm", newAlarm);
                setResult(RESULT_OK, data);

                finish();
                break;

            case R.id.cancelButton:
                showDialogCheckCancel();
                break;

            case R.id.basicSoundLayout:
                setRingtone(1000);
                break;

            case R.id.umbSoundLayout:
                setRingtone(1001);
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
            if (isChecked) {
                for (int i = 1; i < dayButton.length; i++) {
                    dayButton[i].setBackgroundColor(dayTrue);
                    allDayFlag = true;
                    dayArr[i] = true;
                }
            } else {

                for (int i = 1; i < dayArr.length; i++) {
                    if (!dayArr[i]) {
                        return;
                    }
                }

                for (int i = 1; i < dayButton.length; i++) {
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

    public void setRingtone(int requestCode) {
        boolean flag = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = isExternalStorageReadable();
        }

        if (!flag) return;
        Intent intent = new Intent(this, RingtoneListActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode >= 1000) {
            if (resultCode == RESULT_OK) {
                Ringtone ringtone = (Ringtone) data.getSerializableExtra("Ringtone");

                // content://settings/system/ringtone
                switch (requestCode) {
                    case REQUEST_CODE_BASIC_SOUND:
                        if (ringtone != null) {
                            basicSound.setText(ringtone.getTitle());
                            basicSoundUri = ringtone.getUri();
                        }
                        break;
                    case REQUEST_CODE_UMB_SOUND:
                        if (ringtone != null) {
                            umbSound.setText(ringtone.getTitle());
                            umbSoundUri = ringtone.getUri();
                        }
                        break;
                }
            }
        } else if (requestCode >= 100) {
            if (resultCode == RESULT_OK) {
                Location resultLocation = (Location) data.getSerializableExtra("location");

                if (resultLocation != null) {
                    location = resultLocation;

                    String address = location.getStreetAddress();
                    if (address == null) address = location.getLotAddress();
                    currentAddress.setText(address);
                }
            }
        }
    }

    public void showDialogCheckCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CANCEL")
                .setMessage("알람 생성 및 수정을 취소하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(true);
        builder.setNegativeButton("아니오", null);
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isExternalStorageReadable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        showDialogCheckCancel();
    }
}

