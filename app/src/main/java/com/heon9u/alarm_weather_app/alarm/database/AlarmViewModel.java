package com.heon9u.alarm_weather_app.alarm.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.heon9u.alarm_weather_app.dto.Alarm;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class AlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;
    private LiveData<List<Alarm>> allAlarms;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        alarmRepository = new AlarmRepository(application);
        allAlarms = alarmRepository.getAllAlarms();
    }

    public void insert(Alarm alarm) {
        alarmRepository.insert(alarm);
    }

    public void update(Alarm alarm) {
        alarmRepository.update(alarm);
    }

    public void delete(Alarm alarm) {
        alarmRepository.delete(alarm);
    }

    public LiveData<List<Alarm>> getAllAlarms() {return allAlarms;}

    public Single<Alarm> getAlarm(int id) { return alarmRepository.getAlarm(id); }

    public Observable<List<Alarm>> getAllAlarmsFromService() {return alarmRepository.getAllAlarmsFromService();}
}
