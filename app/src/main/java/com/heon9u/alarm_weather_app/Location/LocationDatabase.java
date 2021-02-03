package com.heon9u.alarm_weather_app.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.heon9u.alarm_weather_app.Dto.Location;

import androidx.annotation.Nullable;

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
                "address TEXT," +
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

        cv.put("address", location.getAddress());
        cv.put("latitude", location.getLatitude());
        cv.put("longitude", location.getLongitude());

        db.insert(Location, null, cv);
    }

    public void deleteLocation(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Location, "id=?", new String[] {String.valueOf(id)});
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
