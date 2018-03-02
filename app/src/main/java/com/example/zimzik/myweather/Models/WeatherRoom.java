package com.example.zimzik.myweather.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class WeatherRoom {
    @PrimaryKey(autoGenerate = true) private int uid;
    private String city;
    private String country;
    private int weatherCode;
    private double windSpeed;
    private int windDirectionDegrees;
    private double temperature;
    private int precipitation;
    private long refreshDate;

    public WeatherRoom(String city, String country, int weatherCode, double windSpeed, int windDirectionDegrees, double temperature, int precipitation, long refreshDate) {
        this.city = city;
        this.country = country;
        this.weatherCode = weatherCode;
        this.windSpeed = windSpeed;
        this.windDirectionDegrees = windDirectionDegrees;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.refreshDate = refreshDate;
    }

    public long getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(int refreshDate) {
        this.refreshDate = refreshDate;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirectionDegrees() {
        return windDirectionDegrees;
    }

    public void setWindDirectionDegrees(int windDirectionDegrees) {
        this.windDirectionDegrees = windDirectionDegrees;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }
}
