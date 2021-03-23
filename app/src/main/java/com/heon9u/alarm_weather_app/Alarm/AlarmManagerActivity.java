package com.heon9u.alarm_weather_app.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.Dto.Alarm;

import java.util.Calendar;

public class AlarmManagerActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    AlarmDatabase alarmDB;
    Calendar calendar;
    Intent preIntent, receiverIntent;
    Alarm alarm;

    String REQUEST_STATE;
    Context context;
    long alarmTime, intervalTime = 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preIntent = getIntent();
        REQUEST_STATE = preIntent.getStringExtra("request");
        this.alarm = (Alarm) preIntent.getSerializableExtra("alarm");
        this.context = getApplicationContext();

        alarmDB = new AlarmDatabase(context);
        receiverIntent = new Intent(context, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

//        Log.d("AlarmManagerActivity", REQUEST_STATE);

        switch (REQUEST_STATE) {
            case "reboot":
                if(!checkOnAlarm())
                    setAlarmManager();
                break;
            case "create":
                setAlarmManager();
                break;
            case "cancel":
                if(checkOnAlarm())
                    cancelAlarm();
        }

        finish();
    }

    public boolean checkOnAlarm() {
        PendingIntent checkIntent = PendingIntent.getBroadcast(context,
                alarm.getId(),
                receiverIntent,
                PendingIntent.FLAG_NO_CREATE);

        if(checkIntent == null) return false;
        return true;
    }

    public void setAlarmManager() {
        calendar = Calendar.getInstance();
        setCalendar();
        requestReceiver(alarm.getId());
    }

    public void setCalendar() {
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);
        alarmTime = calendar.getTimeInMillis();

//        String[] day = alarm.getDay().split(",");
//        boolean[] dayFlag = new boolean[8];
//
//        for(int i=0; i<day.length; i++) {
//            if(day[i].equals(""))
//                break;
//            dayFlag[Integer.parseInt(day[i])] = true;
//        }

        receiverIntent.putExtra("alarmId", alarm.getId());

        if(alarmTime <= System.currentTimeMillis())
            alarmTime += intervalTime;
    }

    public void requestReceiver(int requestCode) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //API 23 이상
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent);
        } else {
            // API 23미만
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    alarmTime, pendingIntent);
        }
    }

    public void cancelAlarm() {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getId(),
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
