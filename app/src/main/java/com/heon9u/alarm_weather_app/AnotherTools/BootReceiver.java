package com.heon9u.alarm_weather_app.AnotherTools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.heon9u.alarm_weather_app.Alarm.AlarmDatabase;
import com.heon9u.alarm_weather_app.Alarm.AlarmReceiver;
import com.heon9u.alarm_weather_app.Dto.Alarm;

import java.util.ArrayList;
import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    Context context;
    AlarmDatabase alarmDB;
    ArrayList<Alarm> alarmList;
    AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(intent.ACTION_BOOT_COMPLETED)) {
            this.context = context;
            alarmList = new ArrayList<>();
            getTurnOnAlarmList();

            if(alarmList.size() == 0)
                return;

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            resetAlarmList();
        }
    }

    public void resetAlarmList() {
        Calendar calendar = Calendar.getInstance();
        long alarmTime = 0;
        for(Alarm alarm: alarmList) {
            calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMinute());
            calendar.set(Calendar.SECOND, 0);
            alarmTime = calendar.getTimeInMillis();

            if(alarmTime <= System.currentTimeMillis())
                alarmTime += 1000 * 60 * 60 * 24;

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("alarmId", alarm.getId());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    alarm.getId(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            resetAlarmManager(alarmTime, pendingIntent);
        }
    }

    public void resetAlarmManager(long alarmTime, PendingIntent pendingIntent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //API 23 이상
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    alarmTime, pendingIntent);
        } else {
            // API 23미만
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    alarmTime, pendingIntent);
        }
    }

    public void getTurnOnAlarmList() {
        alarmDB = new AlarmDatabase(context);
        alarmList = alarmDB.readTurnOnAlarm();
        alarmDB.close();
    }
}
