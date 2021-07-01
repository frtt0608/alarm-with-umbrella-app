package com.heon9u.alarm_weather_app.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.heon9u.alarm_weather_app.alarm.database.AlarmDao;
import com.heon9u.alarm_weather_app.alarm.database.AlarmDatabase;
import com.heon9u.alarm_weather_app.alarm.database.AlarmRepository;
import com.heon9u.alarm_weather_app.alarm.database.AlarmViewModel;
import com.heon9u.alarm_weather_app.dto.Alarm;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlarmReceiver extends BroadcastReceiver {

    AlarmDatabase alarmDatabase;
    AlarmDao alarmDao;
    Context context;
    Alarm alarm;
    Intent serviceIntent;
    String alarmDay;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        alarmDatabase = AlarmDatabase.getDatabase(context);
        alarmDao = alarmDatabase.alarmDao();

        setAlarm(intent);
    }

    public void setAlarm(Intent intent) {
        int alarmId = intent.getIntExtra("alarmId", 0);
        alarmDao.getAlarm(alarmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    this.alarm = item;
                    serviceIntent = new Intent(context, AlarmService.class);
                    serviceIntent.putExtra("alarm", alarm);

                    alarmDay = alarm.getDay();
                    if (alarmDay.equals("")) {
                        alarm.setTotalFlag(false);
                        alarmDao.update(alarm);
                    }

                    onService();
                });
    }

    public void onService() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
