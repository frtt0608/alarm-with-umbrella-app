package com.heon9u.alarm_weather_app.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.Dto.Alarm;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    AppDatabaseHelper appDB;
    Calendar calendar;
    Intent receiverIntent;
    Alarm alarm;

    Context context;
    long alarmTime, curTime, intervalTime = 24 * 60 * 60 * 1000;

    AlarmActivity(Context context, int alarmId, String request) {
        this.context = context;
        appDB = new AppDatabaseHelper(context);
        alarm = new Alarm();
        alarm.setId(alarmId);

        if(request.equals("cancel")) {
            cancelAlarm();
        } else {
            setOnAlarm();
            setAlarmManager();
        }
    }

    public void setOnAlarm() {
        Cursor cursor = appDB.readAlarm(alarm.getId());

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

    public void setAlarmManager() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar = Calendar.getInstance();
        curTime = System.currentTimeMillis();
        receiverIntent = new Intent(context, AlarmReceiver.class);

        setCalendar();
    }

    public void setCalendar() {
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);
        alarmTime = calendar.getTimeInMillis();

        receiverIntent.putExtra("alarm", alarm);
        String[] day = alarm.getDay().split(",");

        if(day.length == 0) {
            if(calendar.getTimeInMillis() <= curTime) {
                alarmTime += intervalTime;
            }
        } else {
            boolean[] dayFlag = new boolean[8];
            for(int i=0; i<day.length; i++) {
                dayFlag[Integer.parseInt(day[i])] = true;
            }

            int today = calendar.get(Calendar.DAY_OF_WEEK);
            int alarmDay = 0;

            for(int i=today; i<dayFlag.length; i++) {
                if()
            }
        }


        requestReceiver(alarm.getId());
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //API 19 이상 API 23미만
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        pendingIntent);
            } else {
                //API 19미만
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        pendingIntent);
            }
        }
    }

    public void cancelAlarm() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        receiverIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getId(),
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
