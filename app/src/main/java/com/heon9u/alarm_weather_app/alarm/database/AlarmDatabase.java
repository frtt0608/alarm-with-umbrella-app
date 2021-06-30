package com.heon9u.alarm_weather_app.alarm.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.heon9u.alarm_weather_app.dto.Alarm;

@Database(entities = {Alarm.class}, version = 1)
public abstract class AlarmDatabase extends RoomDatabase {

    private static AlarmDatabase instance;
    public abstract AlarmDao alarmDao();

    public static synchronized AlarmDatabase getDatabase(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AlarmDatabase.class,"alarm_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallBack)
                        .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
