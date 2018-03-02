package com.example.zimzik.myweather.Apis;


import com.example.zimzik.myweather.Models.WeatherRF;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("currentconditions/v1/{city_key}?&details=true")
    Call<List<WeatherRF>> getWeather(@Path("city_key") int cityKey, @Query("apikey") String apiKey);
}
