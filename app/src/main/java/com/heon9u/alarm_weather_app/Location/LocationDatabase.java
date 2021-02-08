package com.heon9u.alarm_weather_app.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;

import com.heon9u.alarm_weather_app.Dto.Location;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

public class LocationDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "WeatherAlarm.db";
    private static final int DATABASE_VERSION = 2;
    private static final String Location = "location";

    public LocationDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + Location +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "streetAddress TEXT," +
                "lotAddress TEXT," +
                "communityCenter TEXT," +
                "latitude DOUBLE," +
                "longitude DOUBLE);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + Location;
        db.execSQL(query);
        onCreate(db);
    }

    public void createLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("streetAddress", location.getStreetAddress());
        cv.put("lotAddress", location.getLotAddress());
        cv.put("communityCenter", location.getCommunityCenter());
        cv.put("latitude", location.getLatitude());
        cv.put("longitude", location.getLongitude());

        db.insert(Location, null, cv);
    }

    public int deleteLocation(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(Location, "id=?", new String[] {String.valueOf(id)});
    }

    public Cursor readAllLocation() {
        String query = "SELECT * FROM " + Location;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readLocation(int id) {
        String query = "SELECT * FROM " + Location + " WHERE id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
