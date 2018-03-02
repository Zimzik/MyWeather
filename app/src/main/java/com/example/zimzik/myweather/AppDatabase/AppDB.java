package com.example.zimzik.myweather.AppDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.zimzik.myweather.DAO.WeatherDao;
import com.example.zimzik.myweather.Models.WeatherRoom;

@Database(entities = {WeatherRoom.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract WeatherDao weatherDao();

    private static AppDB sInstance;

    public static AppDB getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDB.class, "weather.db").build();
        }
        return sInstance;
    }
}
