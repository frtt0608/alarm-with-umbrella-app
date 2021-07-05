package com.heon9u.alarm_weather_app.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.heon9u.alarm_weather_app.alarm.database.AlarmDao;
import com.heon9u.alarm_weather_app.alarm.database.AlarmDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlarmReceiver extends BroadcastReceiver {

    AlarmDao alarmDao;
    Context context;
    Intent serviceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int alarmId = intent.getIntExtra("alarmId", 0);

        AlarmDatabase alarmDatabase = AlarmDatabase.getDatabase(context);
        alarmDao = alarmDatabase.alarmDao();
        serviceIntent = new Intent(context, AlarmService.class);

        getAlarmWithRxJava(alarmId);
    }

    public void getAlarmWithRxJava(int alarmId) {

        alarmDao.getAlarm(alarmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(alarm -> {

                    serviceIntent.putExtra("alarm", alarm);
                    onService();
                });
    }

    public void onService() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
