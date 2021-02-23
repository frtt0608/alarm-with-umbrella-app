package com.heon9u.alarm_weather_app.Alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.heon9u.alarm_weather_app.Dto.Alarm;

public class AlarmDatabase extends SQLiteOpenHelper {

    private Context context;
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "Alarm.db";
    private static final int DATABASE_VERSION = 2;
    private static final String Alarm = "alarm";

    public AlarmDatabase(@Nullable Context context) {
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
                "volume INTEGER," +
                "basicSoundFlag BOOLEAN," +
                "basicSoundTitle TEXT," +
                "basicSoundUri TEXT," +
                "umbSoundFlag BOOLEAN," +
                "umbSoundTitle TEXT," +
                "umbSoundUri TEXT," +
                "vibFlag BOOLEAN," +
                "location_id INTEGER);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + Alarm;
        db.execSQL(query);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("Can't downgrade database from version " +
                oldVersion + " to " + newVersion);
    }

    public void setDatabaseAlarm(Alarm alarm, String mode) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("hour", alarm.getHour());
        cv.put("minute", alarm.getMinute());
        cv.put("title", alarm.getTitle());
        cv.put("totalFlag", true);
        cv.put("allDayFlag", alarm.isAllDayFlag());
        cv.put("day", alarm.getDay());
        cv.put("volume", alarm.getVolume());
        cv.put("basicSoundFlag", alarm.isBasicSoundFlag());
        cv.put("basicSoundTitle", alarm.getBasicSoundTitle());
        cv.put("basicSoundUri", alarm.getBasicSoundUri());
        cv.put("umbSoundFlag", alarm.isUmbSoundFlag());
        cv.put("umbSoundTitle", alarm.getUmbSoundTitle());
        cv.put("umbSoundUri", alarm.getUmbSoundUri());
        cv.put("vibFlag", alarm.isVibFlag());
        cv.put("location_id", alarm.getLocation_id());

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
        db = getWritableDatabase();
        return db.delete("alarm", "id=?", new String[] {String.valueOf(id)});
    }

    public void changeTotalFlag(int id, boolean flag) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("totalFlag", flag);

        db.update(Alarm, cv, "id=?", new String[] {String.valueOf(id)});
    }

    public Cursor readAllAlarm() {
        String query = "SELECT * FROM " + Alarm;
        db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readAlarm(int id) {
        String query = "SELECT * FROM " + Alarm + " WHERE id = " + id;
        db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readLastAlarm() {
        String query = "SELECT * FROM " + Alarm + " ORDER BY id DESC limit 1";
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readTurnOnAlarm() {
        String query = "SELECT * FROM " + Alarm + " WHERE totalFlag = " + true;
        db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor getItemCount() {
        String query = "SELECT COUNT(*) FROM " + Alarm;
        db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}

