package com.heon9u.alarm_weather_app.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.heon9u.alarm_weather_app.dto.Alarm;

public class AlarmReceiver extends BroadcastReceiver {

    AlarmViewModel alarmViewModel;
    Context context;
    Alarm alarm;
    Intent serviceIntent;
    String alarmDay;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        setAlarm(intent);
        serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarm", alarm);

        alarmDay = alarm.getDay();
        if (alarmDay.equals("")) {
            alarm.setTotalFlag(false);
            alarmViewModel.update(alarm);
        }

        onService();
    }

    public void setAlarm(Intent intent) {
        int alarmId = intent.getIntExtra("alarmId", 0);
        alarm = alarmViewModel.getAlarm(alarmId);
    }

    public void onService() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
