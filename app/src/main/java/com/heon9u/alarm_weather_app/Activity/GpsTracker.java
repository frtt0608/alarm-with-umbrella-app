package com.heon9u.alarm_weather_app.Activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class GpsTracker extends Service implements LocationListener {

    private final Context context;
    Location location;
    double latitude, longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 0;
    protected LocationManager locationManager;

    public GpsTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if(isGpsEnabled || isNetworkEnabled) {
                int fineLocationPermission = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int coarseLocationPermission = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

                if(fineLocationPermission != PackageManager.PERMISSION_GRANTED
                        || coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }

                if(isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if(locationManager != null) {
                        // 최근 위치
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            System.out.println("NETWORK_PROVIDER");
                        }
                    }
                }

                if(isGpsEnabled) {

                    if(location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(locationManager != null) {
                            // 최근 위치
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                System.out.println("GPS_PROVIDER");
                            }
                        }
                    }
                }
                if(location == null)
                    Log.d("GpsTracker", latitude + ", " + longitude);
            }

        } catch(Exception e) {
            Log.d("Error", "can't get Location");
        }

        return location;
    }

    public void stopGpsTracker() {
        if(locationManager != null)
            locationManager.removeUpdates(GpsTracker.this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
