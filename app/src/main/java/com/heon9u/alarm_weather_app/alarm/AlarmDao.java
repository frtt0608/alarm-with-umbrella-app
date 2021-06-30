package com.heon9u.alarm_weather_app.alarm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.heon9u.alarm_weather_app.dto.Alarm;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

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

    @Query("SELECT * FROM alarm_table WHERE id = :id")
    Alarm getAlarm(int id);
}
