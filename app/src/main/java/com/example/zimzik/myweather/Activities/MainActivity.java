package com.example.zimzik.myweather.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zimzik.myweather.AppDatabase.AppDB;
import com.example.zimzik.myweather.Models.WeatherRF;
import com.example.zimzik.myweather.Models.WeatherRoom;
import com.example.zimzik.myweather.R;
import com.example.zimzik.myweather.Utils.ServerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = ServerUtil.class.getSimpleName();
    private final int CITY_KEY = 324561;
    private final String API_KEY = "ENZDr78HMqGf7GNwnMayBlurnoSrkHJM";
    private AppDB db;

    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_current_date)
    TextView tvCurrentDate;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_wind_speed)
    TextView tvWindSpeed;
    @BindView(R.id.tv_precipitation)
    TextView tvPrecipitation;
    @BindView(R.id.tv_int_connection_error)
    TextView tvIntConnectionError;
    @BindView(R.id.tv_last_refresh_info)
    TextView tvLastRefreshInfo;
    @BindView(R.id.iv_current_weather)
    ImageView ivCurrentWeather;
    @BindView(R.id.iv_wind_direction)
    ImageView ivWindDirection;
    @BindView(R.id.iv_temperature)
    ImageView ivTemperature;
    @BindView(R.id.iv_settings)
    ImageView ivSettings;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Describe swipe refresh of app
        mSwipeRefreshLayout = findViewById(R.id.refresh);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getWeatherData();
            mSwipeRefreshLayout.setRefreshing(false);
        });

        ivSettings.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        // Init DB
        db = AppDB.getsInstance(this);

        // Get data from server
        getWeatherData();


        // Temporary button, show data from db in log.
        findViewById(R.id.db_connection).setOnClickListener((View v) -> {
            Thread getWeather = new Thread(() -> {
                List<WeatherRoom> list = db.weatherDao().getWeather();
                WeatherRoom weather = list.get(0);
                Log.d(TAG, "Weather code: " + weather.getWeatherCode() + ", Temperature: " + weather.getTemperature() + ", Wind: " + weather.getWindSpeed() + ", list size = " + list.size() + ", Refresh time: " + new Date(weather.getRefreshDate()).toString());
            }, "GetWeather");
            getWeather.start();
            try {
                getWeather.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    private void getWeatherData() {
        ServerUtil.getWeatherApi()
                .getWeather(CITY_KEY, API_KEY)
                .enqueue(new Callback<List<WeatherRF>>() {
                    @Override
                    public void onResponse(Call<List<WeatherRF>> call, Response<List<WeatherRF>> response) {
                        tvIntConnectionError.setText("");
                        tvLastRefreshInfo.setText("");
                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                // Get first element of weather list from server
                                WeatherRF mWeatherData = response.body().get(0);

                                // Get all info about current weather
                                double temperature = mWeatherData.getTemperature();
                                double windSpeed = mWeatherData.getWindSpeed();
                                int precipitation = mWeatherData.getPrecipitation();
                                int weatheryKey = mWeatherData.getWeatherKey();
                                int windDirection = mWeatherData.getWindDirection();

                                // Create new thread to delete wather data on DB.
                                Thread deleteAll = new Thread(() -> db.weatherDao().deleteAll());
                                deleteAll.start();
                                try {
                                    deleteAll.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                //Create new thread to save weather data on db
                                Thread insert = new Thread(() -> db.weatherDao().insert(new WeatherRoom("Lviv", "Ukraine", weatheryKey, windSpeed, windDirection, temperature, precipitation, System.currentTimeMillis())));
                                insert.start();
                                try {
                                    insert.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // Call a method, that set weahter icon
                                setMainWeatherIcon(weatheryKey);
                                // Call a method, that set a text info about weather
                                setTextInfo(temperature, windSpeed, precipitation);
                                // Call a method, that set a wind direction icon
                                setWindDirectionIcon(mWeatherData.getWindDirection());
                                // Call a method, that set a termometr icon dependencing in temperature
                                setTermometrIcon(temperature);
                            } else {
                                Log.d(TAG, "Request body is null!");
                            }
                        } else {
                            Log.d(TAG, "Something wrong with response!");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<WeatherRF>> call, Throwable t) {
                        tvIntConnectionError.setText("Interner connection is not available!");
                        final WeatherRoom[] weather = new WeatherRoom[1];
                        Thread getWeather = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<WeatherRoom> list = db.weatherDao().getWeather();
                                weather[0] = list.get(0);
                            }
                        });
                        getWeather.start();
                        try {
                            getWeather.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Call a method, that set weahter icon
                        setMainWeatherIcon(weather[0].getWeatherCode());
                        // Call a method, that set a text info about weather
                        setTextInfo(weather[0].getTemperature(), weather[0].getWindSpeed(), weather[0].getPrecipitation());
                        // Call a method, that set a wind direction icon
                        setWindDirectionIcon(weather[0].getWindDirectionDegrees());
                        // Call a method, that set a termometr icon dependencing in temperature
                        setTermometrIcon(weather[0].getTemperature());
                        tvLastRefreshInfo.setText(lastRefreshTime(weather[0].getRefreshDate()));

                        Log.d(TAG, "Failure: " + t.getMessage());
                    }
                });
    }

    private String lastRefreshTime(long refreshDate) {
        refreshDate = System.currentTimeMillis() - refreshDate;
        long days = TimeUnit.MILLISECONDS.toDays(refreshDate);
        refreshDate -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(refreshDate);
        refreshDate -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(refreshDate);
        refreshDate -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(refreshDate);

        StringBuilder sb = new StringBuilder();
        sb.append("Last refresh: ");
        if (days > 0) {
            sb.append(days + "d.");
            sb.append(hours + "h. ");
            sb.append(minutes + " m. ");
            sb.append("ago.");
        } else if (days <= 0 && hours > 0) {
            sb.append(hours + "h. ");
            sb.append(minutes + "m. ");
            sb.append("ago.");
        } else if (days <= 0 && minutes > 0) {
            sb.append(minutes + "m. ");
            sb.append(seconds + "s. ");
            sb.append("ago.");
        } else {
            sb.append(seconds + "s. ");
            sb.append("ago");
        }

        return sb.toString();
    }


    private void setTextInfo(double temp, double windSpeed, int prec) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, KK aa", Locale.ENGLISH);
        tvCurrentDate.setText(sdf.format(new Date(System.currentTimeMillis())));
        tvTemperature.setText(Math.round(temp) + " °C");
        tvWindSpeed.setText(Math.round(windSpeed) + " km/h");
        tvPrecipitation.setText(prec + " mm");
        Log.d(TAG, sdf.format(new Date(System.currentTimeMillis())));
    }

    private void setMainWeatherIcon(int weatherKey) {
        if (weatherKey == 3 || weatherKey == 4) {
            ivCurrentWeather.setImageResource(getImageResourceId(2));
        } else if (weatherKey == 6) {
            ivCurrentWeather.setImageResource(getImageResourceId(5));
        } else if (weatherKey == 11) {
            ivCurrentWeather.setImageResource(getImageResourceId(8));
        } else if (weatherKey == 14) {
            ivCurrentWeather.setImageResource(getImageResourceId(13));
        } else if (weatherKey == 17) {
            ivCurrentWeather.setImageResource(getImageResourceId(16));
        } else if (weatherKey == 22) {
            ivCurrentWeather.setImageResource(getImageResourceId(19));
        } else if (weatherKey == 21 || weatherKey == 23) {
            ivCurrentWeather.setImageResource(getImageResourceId(20));
        } else if (weatherKey == 35 || weatherKey == 36) {
            ivCurrentWeather.setImageResource(getImageResourceId(34));
        } else if (weatherKey == 38) {
            ivCurrentWeather.setImageResource(getImageResourceId(37));
        } else if (weatherKey == 40) {
            ivCurrentWeather.setImageResource(getImageResourceId(39));
        } else if (weatherKey == 42) {
            ivCurrentWeather.setImageResource(getImageResourceId(41));
        } else if (weatherKey == 44) {
            ivCurrentWeather.setImageResource(getImageResourceId(43));
        } else ivCurrentWeather.setImageResource(getImageResourceId(weatherKey));
    }

    private void setTermometrIcon(double temp) {
        if (temp >= 30) {
            ivTemperature.setImageResource(R.drawable.ic_weather30);
        } else if (temp < 30 && temp > 0) {
            ivTemperature.setImageResource(R.drawable.ic_termometr_75);
        } else if (temp == 0) {
            ivTemperature.setImageResource(R.drawable.ic_termometr_50);
        } else if (temp < 0 && temp > -30) {
            ivTemperature.setImageResource(R.drawable.ic_termometr_25);
        } else {
            ivTemperature.setImageResource(R.drawable.ic_weather31);
        }
    }

    private void setWindDirectionIcon(int degrees) {
        ivWindDirection.animate().rotation((float) degrees);
    }

    private int getImageResourceId(int weatherkey) {
        return getResources().getIdentifier("ic_weather" + weatherkey, "drawable", getPackageName());
    }
}


