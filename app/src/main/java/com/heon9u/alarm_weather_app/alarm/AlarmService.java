package com.heon9u.alarm_weather_app.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;

import androidx.annotation.Nullable;

import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.dto.CurrentWeather;
import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.location.LocationDatabase;
import com.heon9u.alarm_weather_app.openweather.OpenWeatherApi;
import com.heon9u.alarm_weather_app.R;

import java.util.Calendar;

public class AlarmService extends Service {
    private final String CHANNEL_ID = "OnAlarm";
    private final String CHANNEL_NAME = "OnAlarm";

    final int SERVICE_ID = 1994;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    NotificationManager NM;
    Notification.Builder builder;
    Notification notification;
    Alarm alarm;
    Location location;
    CurrentWeather currentWeather;
    Vibrator vibrator;

    boolean isRain, basicFlag, umbFlag, repeatFlag;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 Activity 사이에서 통신할 때 사용
        // 데이터 전달 용
        return null;
    }

    @Override
    public void onCreate() {
        // 서비스 실행 시, 최초 호출(한번)
        super.onCreate();
        setNotification();
        startForeground(SERVICE_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        setObjectExtra(intent);

        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        String alarmDay = alarm.getDay();

        if (alarmDay.equals("")) {
            repeatFlag = false;
            startAlarmThread();
        } else {
            repeatFlag = true;

            if (alarm.isAllDayFlag() || alarmDay.contains(Integer.toString(today))) {
                startAlarmThread();
            } else {
                stopForeground(true);
            }
        }

        return START_NOT_STICKY;
    }

    public void startAlarmThread() {
        new Thread(() -> {
            searchCurrentForecast();
            setAudioManager();
            startRingtone();
            onPage();
        }).start();

        if (alarm.isVibFlag()) {
            setVibrate();
        }
    }

    public void setObjectExtra(Intent intent) {
        alarm = (Alarm) intent.getSerializableExtra("alarm");
        basicFlag = alarm.isBasicSoundFlag();
        umbFlag = alarm.isUmbSoundFlag();

        if (alarm.getLocation_id() != 0) {
            LocationDatabase locationDB = new LocationDatabase(this);
            location = locationDB.readLocation(alarm.getLocation_id());
            locationDB.close();
        }
    }

    public void setRepeatAlarm() {
        Intent alarmIntent = new Intent(this, AlarmManagerActivity.class);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("request", "create");
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(alarmIntent);
    }

    public void searchCurrentForecast() {
        if (location == null || location.getId() == 0) return;

        Double lat = location.getLatitude();
        Double lon = location.getLongitude();
        String weatherUrl = "?lat=" + lat + "&lon=" + lon + "&units=metric" + "&lang=kr";

        OpenWeatherApi openWeatherApi = new OpenWeatherApi(1);
        openWeatherApi.execute(weatherUrl);

        synchronized (openWeatherApi) {
            try {
                openWeatherApi.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        currentWeather = openWeatherApi.currentWeather;
        String weatherState = currentWeather.getWeather().getMain();

        if (weatherState.equals("Rain") || weatherState.equals("Snow")) {
            isRain = true;
        }
    }

    public void setAudioManager() {
        if (!basicFlag && !umbFlag) return;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        int volume = alarm.getVolume();

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,
                maxVol * volume / 100,
                AudioManager.FLAG_PLAY_SOUND);
    }

    public void startRingtone() {
        if (!basicFlag && !umbFlag) return;

        Uri basicUri = Uri.parse(alarm.getBasicSoundUri());
        Uri umbUri = Uri.parse(alarm.getUmbSoundUri());

        try {
            if (mediaPlayer == null)
                mediaPlayer = new MediaPlayer();

            if (umbFlag && isRain) {
                mediaPlayer.setDataSource(getApplicationContext(), umbUri);
            } else if (basicFlag) {
                mediaPlayer.setDataSource(getApplicationContext(), basicUri);
            }

            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
            });
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();

                mediaPlayer.setAudioAttributes(audioAttributes);
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            }
            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onPage() {
        Intent onIntent = new Intent(this, AlarmOnActivity.class);
        onIntent.putExtra("location", location);
        onIntent.putExtra("weather", currentWeather);
        onIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(onIntent);
    }

    public void setNotification() {
        Intent intent = new Intent(this, AlarmOnActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

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

        setNotificationBuilder(pendingIntent);
    }

    public void setNotificationBuilder(PendingIntent pendingIntent) {
        builder.setContentTitle("우산 챙겨주는 알람시계")
                .setTicker("알람 on")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.alert_light_frame, "알람 해제하기", pendingIntent);

        notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    public void setVibrate() {
        new Thread(() -> {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {1000, 1000, 1000, 1000};
            int repeat = 0; // 0:반복, -1:반복x

            vibrator.vibrate(pattern, repeat);
        }).start();
    }

    public void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void stopVibrate() {
        if (vibrator != null)
            vibrator.cancel();
    }

    @Override
    public void onDestroy() {
        // 서비스 종료 시, 호출
        super.onDestroy();
        stopMediaPlayer();
        stopVibrate();

        if(repeatFlag) {
            setRepeatAlarm();
        }
    }
}
