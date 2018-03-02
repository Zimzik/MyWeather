package com.example.zimzik.myweather.Utils;


import android.util.Log;

import com.example.zimzik.myweather.Apis.WeatherApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerUtil {
    private static final String TAG = ServerUtil.class.getSimpleName();
    private static Retrofit sRetrofit;

    public static WeatherApi getWeatherApi() {
        if (sRetrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor(log -> Log.d(TAG, log)))
                    .build();

            sRetrofit = new Retrofit.Builder()
                    .baseUrl("http://dataservice.accuweather.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return sRetrofit.create(WeatherApi.class);
    }
}
