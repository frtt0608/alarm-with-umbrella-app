package com.heon9u.alarm_weather_app.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

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

            if(alarmList.size() == 0) return;

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
            Log.d("BootReceiver", alarm.toString());
        }
    }

    public void resetAlarmManager(long alarmTime, PendingIntent pendingIntent) {

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

    public void getTurnOnAlarmList() {
        alarmDB = new AlarmDatabase(context);

        Cursor cursor = alarmDB.readTurnOnAlarm();
        if(cursor.getCount() == 0) {
            Log.e("BootReceiver", "All alarms off");
        } else {
            while(cursor.moveToNext()) {
                Alarm alarm = setAlarm(cursor);
                alarmList.add(alarm);
            }
        }
        cursor.close();
    }

    public Alarm setAlarm(Cursor cursor) {
        Alarm alarm = new Alarm();

        alarm.setId(cursor.getInt(0));
        alarm.setHour(cursor.getInt(1));
        alarm.setMinute(cursor.getInt(2));
        alarm.setTitle(cursor.getString(3));
        alarm.setTotalFlag(cursor.getInt(4) > 0);
        alarm.setAllDayFlag(cursor.getInt(5) > 0);
        alarm.setDay(cursor.getString(6));
        alarm.setVolume(cursor.getInt(7));

        alarm.setBasicSoundFlag(cursor.getInt(8) > 0);
        alarm.setBasicSoundTitle(cursor.getString(9));
        alarm.setBasicSoundUri(cursor.getString(10));

        alarm.setUmbSoundFlag(cursor.getInt(11) > 0);
        alarm.setUmbSoundTitle(cursor.getString(12));
        alarm.setUmbSoundUri(cursor.getString(13));

        alarm.setVibFlag(cursor.getInt(14) > 0);
        alarm.setLocation_id(cursor.getInt(15));

        return alarm;
    }
}
