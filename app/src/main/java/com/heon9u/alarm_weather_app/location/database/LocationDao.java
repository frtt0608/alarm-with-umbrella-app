package com.heon9u.alarm_weather_app.location.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.heon9u.alarm_weather_app.dto.Location;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface LocationDao {

    @Insert
    Completable insert(Location location);

    @Update
    Completable update(Location location);

    @Delete
    Completable delete(Location location);

    @Query("SELECT * FROM location_table ORDER BY orderNum")
    LiveData<List<Location>> getAllLocations();

    @Query("SELECT * FROM location_table WHERE id = :id")
    Location getLocation(int id);

    @Query("SELECT MAX(orderNum) FROM location_table")
    int getMaxOrderNum();
}
