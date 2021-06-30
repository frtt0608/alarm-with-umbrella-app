package com.heon9u.alarm_weather_app.location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.heon9u.alarm_weather_app.dto.Location;
import com.heon9u.alarm_weather_app.dto.LocationBuilder;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocationDatabase extends SQLiteOpenHelper {

    SQLiteDatabase db;
    private Context context;
    private static final String DATABASE_NAME = "Location.db";
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
                "orderNum INTEGER," +
                "streetAddress TEXT," +
                "lotAddress TEXT," +
                "communityCenter TEXT," +
                "latitude DOUBLE," +
                "longitude DOUBLE," +
                "tmX DOUBLE," +
                "tmY DOUBLE);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + Location;
        db.execSQL(query);
        onCreate(db);
    }

    public void createLocation(Location location) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("orderNum", getMaxOrderNum() + 1);
        cv.put("streetAddress", location.getStreetAddress());
        cv.put("lotAddress", location.getLotAddress());
        cv.put("communityCenter", location.getCommunityCenter());
        cv.put("latitude", location.getLatitude());
        cv.put("longitude", location.getLongitude());
        cv.put("tmX", location.getTmX());
        cv.put("tmY", location.getTmY());

        db.insert(Location, null, cv);
    }

    public int getMaxOrderNum() {
        int orderNum = 0;
        db = getReadableDatabase();
        String query = "SELECT MAX(orderNum) FROM " + Location;

        if(db != null) {
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToNext()) {
                orderNum = cursor.getInt(0);
            }
        }

        return orderNum;
    }

    public int deleteLocation(int id) {
        db = getWritableDatabase();
        return db.delete(Location, "id=?", new String[] {String.valueOf(id)});
    }

    public ArrayList<Location> readAllLocation() {
        String query = "SELECT * FROM " + Location + " ORDER BY orderNum";
        db = getReadableDatabase();
        ArrayList<Location> locationList = new ArrayList<>();
        Location location;

        if(db != null) {
            Cursor cursor = db.rawQuery(query, null);

            while(cursor.moveToNext()) {
                location = setLocationObject(cursor);
                locationList.add(location);
            }
            cursor.close();
        }

        return locationList;
    }

    public Location readFirstLocation() {
        String query = "SELECT * FROM " + Location + " ORDER BY orderNum LIMIT 1";
        db = getReadableDatabase();
        Location location = null;

        if(db != null) {
            Cursor cursor = db.rawQuery(query, null);

            while(cursor.moveToNext()) {
                location = setLocationObject(cursor);
            }

            cursor.close();
        }

        return location;
    }

    public Location readLocation(int id) {
        String query = "SELECT * FROM " + Location + " WHERE id = " + id;
        db = getReadableDatabase();
        Location location = new Location();

        if(db != null) {
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToNext();
            location = setLocationObject(cursor);
            cursor.close();
        }

        return location;
    }

    public Location setLocationObject(Cursor cursor) {
        Location location = new Location();

        if(cursor.getCount() == 0) {
            Log.e("Location Error", "There is not location object");
        } else {
            location = new LocationBuilder()
                    .setId(cursor.getInt(0))
                    .setOrderNum(cursor.getInt(1))
                    .setStreetAddress(cursor.getString(2))
                    .setLotAddress(cursor.getString(3))
                    .setCommunityCenter(cursor.getString(4))
                    .setLatitude(cursor.getDouble(5))
                    .setLongitude(cursor.getDouble(6))
                    .setTmX(cursor.getDouble(7))
                    .setTmY(cursor.getDouble(8))
                    .build();
        }

        return location;
    }

    public void updateOrderNum(List<Location> locationList) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Location location;

        for(int orderNum=0; orderNum<locationList.size(); orderNum++) {
            location = locationList.get(orderNum);
            cv.put("orderNum", orderNum+1);
            db.update(Location, cv, "id=?", new String[] {String.valueOf( location.getId() )});
        }
    }

    public Cursor readLastLocation() {
        String query = "SELECT * FROM " + Location + " ORDER BY id DESC limit 1";
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor getItemCount() {
        String query = "SELECT COUNT(*) FROM " + Location;
        db = getReadableDatabase();
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
