package com.heon9u.alarm_weather_app.alarm;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heon9u.alarm_weather_app.dto.Alarm;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlarmRepository {
    private AlarmDao alarmDao;
    private LiveData<List<Alarm>> allAlarms;

    public AlarmRepository(Application application) {
        AlarmDatabase database = AlarmDatabase.getDatabase(application);
        alarmDao = database.alarmDao();
        allAlarms = alarmDao.getAllAlarms();
    }

    public LiveData<List<Alarm>> getAllAlarms() {
        return allAlarms;
    }

    public Alarm getAlarm(int id) {
        return alarmDao.getAlarm(id);
    }

    // Need to update --> RxJava
    public int getCount() {
        return alarmDao.getCount();
    }

    public void insert(Alarm alarm) {
        alarmDao.insert(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void update(Alarm alarm) {
        alarmDao.update(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void delete(Alarm alarm) {
        alarmDao.delete(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
