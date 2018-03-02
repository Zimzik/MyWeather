package com.example.zimzik.myweather.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zimzik.myweather.R;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        findViewById(R.id.cancel_button).setOnClickListener(v -> {
            finish();
        });
    }
}
