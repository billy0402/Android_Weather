package edu.ntub.weather;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.ntub.weather.dto.Weather;
import edu.ntub.weather.helper.DataHelper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Weather weather = DataHelper.getWeather(MainActivity.this, "data");
    }
}
