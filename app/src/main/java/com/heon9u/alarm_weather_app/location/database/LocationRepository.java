package com.heon9u.alarm_weather_app.location.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.heon9u.alarm_weather_app.dto.Location;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LocationRepository {
    private LocationDao locationDao;
    private LiveData<List<Location>> allLocations;

    public LocationRepository(Application application) {
        LocationDatabase database = LocationDatabase.getDatabase(application);
        locationDao = database.locationDao();
        allLocations = locationDao.getAllLocations();
    }

    public LiveData<List<Location>> getAllLocations() {
        return allLocations;
    }

    public Location getLocation(int id) {
        return locationDao.getLocation(id);
    }

    public int getMaxOrderNum() {
        return locationDao.getMaxOrderNum();
    }

    public void insert(Location location) {
        locationDao.insert(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void update(Location location) {
        locationDao.update(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void delete(Location location) {
        locationDao.delete(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
