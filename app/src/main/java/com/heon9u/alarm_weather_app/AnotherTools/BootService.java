package com.heon9u.alarm_weather_app.AnotherTools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.heon9u.alarm_weather_app.Alarm.AlarmDatabase;
import com.heon9u.alarm_weather_app.Alarm.AlarmManagerActivity;
import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class BootService extends Service {

    private final String CHANNEL_ID = "BootAlarm";
    private final String CHANNEL_NAME = "Bootlarm";

    final int SERVICE_ID = 1993;
    NotificationManager NM;
    Notification.Builder builder;
    Notification notification;
    ArrayList<Alarm> alarmList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // 서비스 실행 시, 최초 호출(한번)
        super.onCreate();
        setNotification();
        startForeground(SERVICE_ID, notification);

        new Thread(() -> {
            getTurnOnAlarmList();
            if(alarmList.size() > 0) {
                resetAlarm();
            }

            stopForeground(true);
        }).start();
    }

    public void getTurnOnAlarmList() {
        AlarmDatabase alarmDB = new AlarmDatabase(this);
        alarmList = alarmDB.readTurnOnAlarm();
        alarmDB.close();
    }

    public void resetAlarm() {
        Intent alarmIntent;

        for(Alarm alarm: alarmList) {
            alarmIntent = new Intent(this, AlarmManagerActivity.class);
            alarmIntent.putExtra("alarm", alarm);
            alarmIntent.putExtra("request", "create");
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(alarmIntent);
        }
    }

    public void setNotification() {

        if (Build.VERSION.SDK_INT >= 26) {
            NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NM.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        setNotificationBuilder();
    }

    public void setNotificationBuilder() {
        builder.setContentTitle("우산 챙겨주는 알람시계")
                .setContentText("알람 재부팅 중...")
                .setSmallIcon(R.drawable.notification_icon);

        notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
    }
}
