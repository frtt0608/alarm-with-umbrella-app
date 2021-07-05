package com.heon9u.alarm_weather_app.alarm.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.heon9u.alarm_weather_app.dto.Alarm;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface AlarmDao {

    @Insert
    Completable insert(Alarm alarm);

    @Update
    Completable update(Alarm alarm);

    @Delete
    Completable delete(Alarm alarm);

    @Query("SELECT * FROM alarm_table")
    LiveData<List<Alarm>> getAllAlarms();

    @Query("SELECT * FROM alarm_table")
    Observable<List<Alarm>> getAllAlarmsFromService();

    @Query("SELECT * FROM alarm_table WHERE id = :id")
    Single<Alarm> getAlarm(int id);
}
