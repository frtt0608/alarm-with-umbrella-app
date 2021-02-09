package com.heon9u.alarm_weather_app.Alarm;

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
import android.util.Log;

import androidx.annotation.Nullable;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.Dto.HourlyWeather;
import com.heon9u.alarm_weather_app.Dto.Location;
import com.heon9u.alarm_weather_app.Openweather.HourlyForecast;
import com.heon9u.alarm_weather_app.R;

import java.io.Serializable;

public class AlarmService extends Service {
    private final static String openWeatherUrl = "https://api.openweathermap.org/data/2.5/onecall";
    private final static String apiKey = "6e20ff161911d310524f6a26ac649500";

    final int SERVICE_ID = 1994;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    PowerManager powerManager;
    NotificationManager NM;
    Notification.Builder builder;
    Notification notification;
    Alarm alarm;
    Location location;
    Vibrator vibrator;

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("AlarmService", "onStartCommand");
        startForeground(SERVICE_ID, notification);
        alarm = (Alarm) intent.getSerializableExtra("alarm");

        Serializable serializable = intent.getSerializableExtra("location");
        if(serializable != null)
            location = (Location) intent.getSerializableExtra("location");

        new Thread(new Runnable() {
            @Override
            public void run() {
                setRingtone();
                onPage();
            }
        }).start();

        if (alarm.isVibFlag()) {
            setVibrate();
        }

        return START_NOT_STICKY;
    }

    public void setRingtone() {
        boolean basicFlag = alarm.isBasicSoundFlag();
        boolean umbFlag = alarm.isUmbSoundFlag();

        if(!basicFlag && !umbFlag) return;

        Log.d("AlarmService", "알람음 체크하기");

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = alarm.getVolume();
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                maxVol*volume/100,
                AudioManager.FLAG_PLAY_SOUND);

        boolean isRain = false;
        if(umbFlag && location != null) {
            // 일정 or 알람시간에 비가 오지 확인하기.
            isRain = searchHourlyForecast();
        }

        startRingtone(basicFlag, umbFlag, isRain);
    }

    public boolean searchHourlyForecast() {
        Double lat = location.getLatitude();
        Double lon = location.getLongitude();

        String hourlyUrl = openWeatherUrl + "?lat=" + lat + "&lon=" + lon +
                "&appid=" + apiKey + "&units=metric" + "&lang=kr";

        HourlyForecast hourlyForecast = new HourlyForecast();
        hourlyForecast.execute(hourlyUrl);
        while(!hourlyForecast.isFinish) { }

        HourlyWeather[] hourlyWeathers = hourlyForecast.hourlyWeathers;
        Log.d("AlarmService", hourlyWeathers[0].toString());

        for(int i=0; i<24; i++) {
            if(hourlyWeathers[i].getRain1h() > 0)
                return true;
        }

        return false;
    }

    public void startRingtone(boolean basicFlag, boolean umbFlag, boolean isRain) {

        Uri basicUri = Uri.parse(alarm.getBasicSound());
        Uri umbUri = Uri.parse(alarm.getUmbSound());
        mediaPlayer = new MediaPlayer();

        if(umbFlag && isRain) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), umbUri);
        } else if(basicFlag) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), basicUri);
        }

        setMediaPlayer();
        mediaPlayer.start();
    }

    public void setMediaPlayer() {
        
        // 21에 추가된 attributes
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            mediaPlayer.setAudioAttributes(audioAttributes);
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    public void onPage() {
        Intent onIntent = new Intent(getApplicationContext(), AlarmOnActivity.class);
        onIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(onIntent);
    }

    public void setNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            Intent intent = new Intent(this, AlarmOnActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= 26) {
                String CHANNEL_ID = "OnAlarm";
                String CHANNEL_NAME = "OnAlarm";
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                NM.createNotificationChannel(notificationChannel);
                builder = new Notification.Builder(this, CHANNEL_ID);
            } else {
                builder = new Notification.Builder(this);
            }

            setNotificationBuilder(pendingIntent);
        }
    }

    public void setNotificationBuilder(PendingIntent pendingIntent) {
        builder.setContentTitle("우산 챙겨주는 알람시계")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker("Alarm on!")
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.alert_light_frame, "알람 해제하기", pendingIntent);

        notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    public void setVibrate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {1000, 1000, 1000, 1000};
                int repeat = 0; // 0:반복, -1:반복x

                vibrator.vibrate(pattern, repeat);
                Log.d("AlarmService", "vibrate on");
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        // 서비스 종료 시, 호출
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        vibrator.cancel();
    }
}
