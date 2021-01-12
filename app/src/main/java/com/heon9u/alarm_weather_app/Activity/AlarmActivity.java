package com.heon9u.alarm_weather_app.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.heon9u.alarm_weather_app.Dto.Alarm;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private PendingIntent pendingIntent;
    private AppDatabaseHelper appDB;
    private Calendar calendar;
    Context context;
    int toDay;
    long curTime;

    ArrayList<Alarm> onAlarmList;

    AlarmActivity(Context context, ArrayList<Alarm> onAlarmList) {
        this.context = context;
        this.onAlarmList = new ArrayList<>();

        appDB = new AppDatabaseHelper(context);

        for(Alarm onAlarm: onAlarmList) {
            Cursor cursor = appDB.readAlarm(onAlarm.getId());

            if(cursor.getCount() == 0) {
                Toast.makeText(context, "No alarm", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    Alarm alarm = new Alarm();

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

                    this.onAlarmList.add(alarm);
                }
            }
        }

        setAlarmManager();
    }

    public void setAlarmManager() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        toDay = calendar.get(Calendar.DAY_OF_WEEK);
        curTime = calendar.getTimeInMillis();
        final Intent receiverIntent = new Intent(context, AlarmReceiver.class);

        for(Alarm alarm: onAlarmList) {
            setCalendar(alarm);
        }
    }

    public void setCalendar(Alarm alarm) {
        String[] dayArr = alarm.getDay().split(",");

        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if(dayArr.length == 0) {
            if(calendar.getTimeInMillis() < curTime) {
                toDay += 1;
                if(toDay > 7) toDay = 1;

                calendar.set(Calendar.DAY_OF_WEEK, toDay);
            }

        } else {
            for(String day: dayArr) {
                calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(day));

            }
        }
    }
}
