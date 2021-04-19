package com.heon9u.alarm_weather_app.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.heon9u.alarm_weather_app.Dto.Alarm;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    AlarmDatabase alarmDB;
    Context context;
    Alarm alarm;
    Intent serviceIntent;
    String alarmDay;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        alarmDB = new AlarmDatabase(context);

        setAlarm(intent);
        serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarm", alarm);

        alarmDay = alarm.getDay();
        if (alarmDay.equals("")) {
            alarmDB = new AlarmDatabase(context);
            alarmDB.changeTotalFlag(alarm.getId(), false);
        }

        onService();
    }

    public void setAlarm(Intent intent) {
        int alarmId = intent.getIntExtra("alarmId", 0);
        alarm = alarmDB.readAlarm(alarmId);
    }

    public void onService() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
