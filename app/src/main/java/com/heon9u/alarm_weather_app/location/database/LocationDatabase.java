package com.heon9u.alarm_weather_app.location.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.heon9u.alarm_weather_app.dto.Location;

@Database(entities = {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {

    private static LocationDatabase instance;
    public abstract LocationDao locationDao();

    public static synchronized LocationDatabase getDatabase(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    LocationDatabase.class, "location_database")
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
