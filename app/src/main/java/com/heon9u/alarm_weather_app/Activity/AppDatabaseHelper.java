package com.heon9u.alarm_weather_app.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.heon9u.alarm_weather_app.Dto.Alarm;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "WeatherAlarm.db";
    private static final int DATABASE_VERSION = 1;

    private static final String Alarm = "alarm";

    public AppDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + Alarm +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "hour INTEGER," +
                "minute INTEGER," +
                "title TEXT," +
                "totalFlag BOOLEAN," +
                "allDayFlag BOOLEAN," +
                "day TEXT," +
                "basicSoundFlag BOOLEAN," +
                "basicSound TEXT," +
                "umbSoundFlag BOOLEAN," +
                "umbSound TEXT," +
                "vibFlag BOOLEAN);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + Alarm;
        db.execSQL(query);
        onCreate(db);
    }



    void setDatabaseAlarm(Alarm alarm, String mode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("hour", alarm.getHour());
        cv.put("minute", alarm.getMinute());
        cv.put("title", alarm.getTitle());
        cv.put("totalFlag", true);
        cv.put("allDayFlag", alarm.isAllDayFlag());
        cv.put("day", alarm.getDay());
        cv.put("basicSoundFlag", alarm.isBasicSoundFlag());
        cv.put("basicSound", alarm.getBasicSound());
        cv.put("umbSoundFlag", alarm.isUmbSoundFlag());
        cv.put("umbSound", alarm.getUmbSound());
        cv.put("vibFlag", alarm.isVibFlag());

        long result;
        if(mode.equals("create")) {
            result = db.insert(Alarm, null, cv);
        } else {
            result = db.update(Alarm, cv, "id=?", new String[] {String.valueOf(alarm.getId())});
        }

        if (result != -1) {
            Toast.makeText(context, "Successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public int deleteAlarm(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("alarm", "id=?", new String[] {String.valueOf(id)});
    }

    void changeTotalFlag(int id, boolean flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("totalFlag", flag);

        db.update(Alarm, cv, "id=?", new String[] {String.valueOf(id)});
    }

    Cursor readAllAlarm() {
        String query = "SELECT * FROM " + Alarm;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    Cursor readAlarm(int id) {
        String query = "SELECT * FROM " + Alarm + " WHERE id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
