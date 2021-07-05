package com.heon9u.alarm_weather_app.location.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.heon9u.alarm_weather_app.dto.Location;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;

public class LocationViewModel extends AndroidViewModel {
    private LocationRepository locationRepository;
    private LiveData<List<Location>> allLocations;


    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository = new LocationRepository(application);
        allLocations = locationRepository.getAllLocations();
    }

    public void insert(Location location) {
        locationRepository.insert(location);
    }

    public void update(Location location) {
        locationRepository.update(location);
    }

    public void delete(Location location) {
        locationRepository.delete(location);
    }

    public Maybe<Location> getLocation(int id) {
        return locationRepository.getLocation(id);
    }

    public LiveData<List<Location>> getAllLocations() {
        return allLocations;
    }
}
