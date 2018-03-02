package com.example.zimzik.myweather.Models;


import com.google.gson.annotations.SerializedName;

public class WeatherRF {

    private class Temperature {
        @SerializedName("Metric")
        private TempMetric mTempMetric;
    }

    private class TempMetric {
        @SerializedName("Value")
        private double mValue;
    }

    private class Wind {
        @SerializedName("Direction")
        private Direction mDirection;
        @SerializedName("Speed")
        private WindSpeed mWindSpeed;
    }

    private class Direction {
        @SerializedName("Degrees")
        private int mWindDirectionDegrees;
    }

    private class WindSpeed {
        @SerializedName("Metric")
        private WindSpeedMetric mWindSpeedMetric;
    }

    private class WindSpeedMetric {
        @SerializedName("Value")
        private Double mWindSpeedValue;
    }

    private class PrecipitationSummary {
        @SerializedName("Precipitation")
        private Precipitation mPrecipitation;
    }

    private class Precipitation {
        @SerializedName("Metric")
        private PrepMetric mPrepMetric;
    }

    private class PrepMetric {
        @SerializedName("Value")
        private Integer mPrepValue;
    }

    @SerializedName("WeatherIcon")
    private int mWeatherKey;

    @SerializedName("Temperature")
    private Temperature mTemperature;

    @SerializedName("Wind")
    private Wind mWind;

    @SerializedName("PrecipitationSummary")
    private PrecipitationSummary mPrecipitationSummary;

    public int getWeatherKey() {
        return mWeatherKey;
    }

    public double getTemperature() {
        return mTemperature.mTempMetric.mValue;
    }

    public Double getWindSpeed() {
        return mWind.mWindSpeed.mWindSpeedMetric.mWindSpeedValue;
    }

    public int getWindDirection() {
        return mWind.mDirection.mWindDirectionDegrees;
    }

    public Integer getPrecipitation() {
        return mPrecipitationSummary.mPrecipitation.mPrepMetric.mPrepValue;
    }
}
