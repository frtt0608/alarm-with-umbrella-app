package com.heon9u.alarm_weather_app.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.Location.LocationDatabase;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    AlarmDatabase alarmDB;
    LocationDatabase locationDB;
    Context context;
    Alarm alarm;
    Calendar calendar;
    Intent serviceIntent;
    String alarmDay;
    Location location;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        calendar = Calendar.getInstance();
        alarmDB = new AlarmDatabase(context);
        locationDB = new LocationDatabase(context);

        int today = calendar.get(Calendar.DAY_OF_WEEK);
        int alarmId = intent.getIntExtra("alarmId", 0);

        setAlarm(alarmId);
        int location_id = 0;
        if((Integer) alarm.getLocation_id() != null) {
            location_id = alarm.getLocation_id();
        }

        if(location_id != 0) {
            setLocation(location_id);
        }

        alarmDay = alarm.getDay();
        serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarm", alarm);
        serviceIntent.putExtra("location", location);

        if (alarmDay.equals("")) {
            alarmDB = new AlarmDatabase(context);
            alarmDB.changeTotalFlag(alarm.getId(), false);

            onService();
        } else {
            setRepeatAlarm();

            if (alarm.isAllDayFlag() || alarmDay.contains(Integer.toString(today))) {
                onService();
            }
        }
    }

    public void setAlarm(int alarmId) {
        alarm = alarmDB.readAlarm(alarmId);
    }

    public void setLocation(int location_id) {
        location = locationDB.readLocation(location_id);
        locationDB.close();
    }

    public void onService() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    public void setRepeatAlarm() {
        Log.d("AlarmReceiver", "알람 반복 설정하기");
        Calendar rCalendar = Calendar.getInstance();
        rCalendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        rCalendar.set(Calendar.MINUTE, alarm.getMinute());
        rCalendar.set(Calendar.SECOND, 0);
        long alarmTime = calendar.getTimeInMillis() + 1000 * 60 * 60 * 24;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent repeatIntent = new Intent(context, AlarmReceiver.class);
        repeatIntent.putExtra("alarmId", alarm.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getId(),
                repeatIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //API 23 이상
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    alarmTime, pendingIntent);
        } else  {
            // API 23미만
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        alarmTime, pendingIntent);

        }
    }
}
