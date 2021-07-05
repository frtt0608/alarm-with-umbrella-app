package com.heon9u.alarm_weather_app.anotherTools;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.heon9u.alarm_weather_app.alarm.AlarmReceiver;
import com.heon9u.alarm_weather_app.alarm.database.AlarmDao;
import com.heon9u.alarm_weather_app.alarm.database.AlarmDatabase;
import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.R;

import java.util.Calendar;
import java.util.List;

public class BootService extends Service {

    private final String CHANNEL_ID = "BootAlarm";
    private final String CHANNEL_NAME = "BootAlarm";
    private final int SERVICE_ID = 1993;
    private final long INTERVAL_TIME = 24 * 60 * 60 * 1000;

    long alarmTime;
    NotificationManager NM;
    Notification.Builder builder;
    Notification notification;
    Context context;
    Intent receiverIntent;
    AlarmManager alarmManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        setNotification();
        startForeground(SERVICE_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmDatabase alarmDatabase = AlarmDatabase.getDatabase(getApplicationContext());
        AlarmDao alarmDao = alarmDatabase.alarmDao();

        alarmDao.getAllAlarmsFromService()
                .subscribe(alarms -> {

                    new Thread(() -> {
                        if (alarms.size() > 0) {
                            resetAlarms(alarms);
                        }

                        stopService(intent);
                    }).start();
                });

        return START_NOT_STICKY;
    }

    public void resetAlarms(List<Alarm> alarms) {
        receiverIntent = new Intent(context, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        for (Alarm alarm: alarms) {
            if (alarm.isTotalFlag()) {
                setCalendar(alarm);
                requestReceiver(alarm.getId());
            }
        }
    }

    public void setCalendar(Alarm alarm) {
        receiverIntent.putExtra("alarmId", alarm.getId());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        alarmTime = calendar.getTimeInMillis();
        if (alarmTime <= System.currentTimeMillis())
            alarmTime += INTERVAL_TIME;
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
            // API 23미만
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    alarmTime, pendingIntent);
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
