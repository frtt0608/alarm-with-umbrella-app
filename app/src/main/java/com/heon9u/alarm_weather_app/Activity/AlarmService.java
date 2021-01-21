package com.heon9u.alarm_weather_app.Activity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Service객체와 Activity사이에서 통신할 때 사용
        // 데이터 전달 용
        return null;
    }

    @Override
    public void onCreate() {
        // 서비스 실행 시, 최초 호출(한번)
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // 서비스 종료 시, 호출
        super.onDestroy();
    }
}
