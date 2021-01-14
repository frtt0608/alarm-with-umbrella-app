package com.heon9u.alarm_weather_app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import com.heon9u.alarm_weather_app.Dto.Alarm;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    AppDatabaseHelper appDB;
    Context context;
    Alarm alarm;
    Calendar calendar;
    Intent serviceIntent;

    String alarmDay;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        appDB = new AppDatabaseHelper(context);
        int alarmId = intent.getIntExtra("alarmId", 0);
        alarm = new Alarm();
        setAlarm(alarmId);
        alarmDay = alarm.getDay();

        calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        if (alarmDay.length() == 0) {
            // alarm on
            // and alarm off (not repeat)
            AppDatabaseHelper appDB = new AppDatabaseHelper(context);
            appDB.changeTotalFlag(alarm.getId(), false);
            serviceIntent = new Intent(context, AlarmService.class);
            System.out.println("일회용 알람: " + calendar.getTime().toString());
        } else {
            if (alarm.isAllDayFlag() || alarmDay.contains(Integer.toString(today))) {
                // check between DAY_OF_WEEK and day
                // if true -> alarm on/reSetting(24h)
                // if false -> alarm reSetting(24h)
                serviceIntent = new Intent(context, AlarmService.class);
                System.out.println("반복 알람: " + calendar.getTime().toString());
            } else {
                System.out.println("Not today!!");
            }

            repeatAlarm();
        }
    }

    public void setAlarm(int getId) {
        Cursor cursor = appDB.readAlarm(getId);

        if(cursor.getCount() == 0) {
            Toast.makeText(context, "No alarm", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToNext();

            alarm.setId(cursor.getInt(0));
            alarm.setHour(cursor.getInt(1));
            alarm.setMinute(cursor.getInt(2));
            alarm.setTitle(cursor.getString(3));
            alarm.setTotalFlag(cursor.getInt(4) > 0);
            alarm.setAllDayFlag(cursor.getInt(5) > 0);
            alarm.setDay(cursor.getString(6));
            alarm.setBasicSoundFlag(cursor.getInt(7) > 0);
            alarm.setBasicSound(cursor.getString(8));
            alarm.setUmbSoundFlag(cursor.getInt(9) > 0);
            alarm.setUmbSound(cursor.getString(10));
            alarm.setVibFlag(cursor.getInt(11) > 0);
        }
    }

    public void repeatAlarm() {
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("request", "create");
    }
}
