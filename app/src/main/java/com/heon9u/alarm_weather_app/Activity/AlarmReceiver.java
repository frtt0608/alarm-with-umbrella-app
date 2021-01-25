package com.heon9u.alarm_weather_app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
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
        Log.d("Receiver", "onReceive");
        this.context = context;
        calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        appDB = new AppDatabaseHelper(context);
        int alarmId = intent.getIntExtra("alarmId", 0);
        alarm = new Alarm();
        setAlarm(alarmId);
        alarmDay = alarm.getDay();
        serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarm", alarm);

        if (alarmDay.equals("")) {
            // alarm on
            // and alarm off (not repeat)
            Log.d("Receiver", "일회용 알람");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    appDB = new AppDatabaseHelper(context);
                    appDB.changeTotalFlag(alarm.getId(), false);
                }
            }).start();

            onService();
        } else {
            if (alarm.isAllDayFlag() || alarmDay.contains(Integer.toString(today))) {
                // check between DAY_OF_WEEK and day
                // if true -> alarm on/reSetting(24h)
                // if false -> alarm reSetting(24h)
                Log.d("Receiver", "반복 알람");
                onService();
            } else {
                Log.d("Receiver", "Not today!!");
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
            alarm.setVolume(cursor.getInt(7));
            alarm.setBasicSoundFlag(cursor.getInt(8) > 0);
            alarm.setBasicSound(cursor.getString(9));
            alarm.setUmbSoundFlag(cursor.getInt(10) > 0);
            alarm.setUmbSound(cursor.getString(11));
            alarm.setVibFlag(cursor.getInt(12) > 0);
        }
    }

    public void onService() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    public void repeatAlarm() {
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("request", "create");
        context.startActivity(alarmIntent);
    }
}