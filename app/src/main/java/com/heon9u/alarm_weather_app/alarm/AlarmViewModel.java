package com.heon9u.alarm_weather_app.alarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heon9u.alarm_weather_app.dto.Alarm;

import java.util.List;

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

    public Alarm getAlarm(int id) { return alarmRepository.getAlarm(id); }

    public LiveData<List<Alarm>> getAllAlarms() {return allAlarms;}

    public int getCount() {return alarmRepository.getCount();}
}
