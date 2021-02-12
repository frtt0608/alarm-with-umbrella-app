package com.heon9u.alarm_weather_app.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

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

    int location_id;
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
        location_id = alarm.getLocation_id();
        if(location_id != 0) {
            setLocation();
        }

        alarmDay = alarm.getDay();
        serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarm", alarm);
        serviceIntent.putExtra("location", location);

        if (alarmDay.equals("")) {
            // alarm on
            // and alarm off (not repeat)
            Log.d("Receiver", "일회용 알람");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    alarmDB = new AlarmDatabase(context);
                    alarmDB.changeTotalFlag(alarm.getId(), false);
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

    public void setAlarm(int alarmId) {

        Cursor cursor = alarmDB.readAlarm(alarmId);

        if(cursor.getCount() == 0) {
            Toast.makeText(context, "No alarm", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToNext();

            alarm = new Alarm();
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
            alarm.setLocation_id(cursor.getInt(13));
        }

        alarmDB.close();
    }

    public void setLocation() {
        Cursor cursor = locationDB.readLocation(location_id);

        if(cursor.getCount() == 0) {
            Toast.makeText(context, "No alarm", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToNext();

            location = new Location();
            location.setId(cursor.getInt(0));
            location.setStreetAddress(cursor.getString(1));
            location.setLotAddress(cursor.getString(2));
            location.setCommunityCenter(cursor.getString(3));
            location.setLatitude(cursor.getDouble(4));
            location.setLongitude(cursor.getDouble(5));
        }

        locationDB.close();
    }

    public void onService() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    public void repeatAlarm() {
        Intent alarmIntent = new Intent(context, AlarmManagerActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("request", "create");
        context.startActivity(alarmIntent);
    }
}
