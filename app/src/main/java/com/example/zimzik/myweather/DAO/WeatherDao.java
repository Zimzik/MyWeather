package com.example.zimzik.myweather.DAO;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.widget.ListView;

import com.example.zimzik.myweather.Models.WeatherRoom;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weatherRoom")
    List<WeatherRoom> getWeather();

    @Insert
    void insert(WeatherRoom weather);

    @Query("DELETE FROM weatherRoom")
    void deleteAll();

}
