package edu.ntub.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ntub.weather.dto.Data;
import edu.ntub.weather.dto.Location;
import edu.ntub.weather.dto.Time;
import edu.ntub.weather.dto.Weather;
import edu.ntub.weather.helper.DataHelper;
import edu.ntub.weather.network.RetrofitConfig;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.LocationClickListener {
    private final String TAG = "MainActivity";
    private Spinner spnDate;
    private RecyclerView recyclerViewWeather;
    private MainRecyclerViewAdapter mainRecyclerViewAdapter;
    private List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spnDate = findViewById(R.id.spnDate);
        recyclerViewWeather = findViewById(R.id.recyclerViewWeather);

        getLocations();
        createSpnDate();
        createRecyclerViewWeather();
    }

    private void getLocations() {
        Thread thread = new Thread(() -> {
            Data data = RetrofitConfig.getWeather();
            Weather weather;

            if (data != null) {
                weather = data.content;
            } else {
                weather = DataHelper.getWeather(MainActivity.this, "data");
            }

            locations = weather.dataSet.locations;
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<String> getDates() {
        List<Time> times = locations.get(0).weatherElements.get(0).times;
        Set<String> weekdaySet = new HashSet<>();

        for (int i = 0; i < times.size() - 1; i++) {
            LocalDateTime time = times.get(i).getMiddleLocalDateTime();
            String weekday = Time.getStringFormat(time, "yyyy-MM-dd E");
            weekdaySet.add(weekday);
        }

        List<String> weekdays = new ArrayList<>(weekdaySet);
        Collections.sort(weekdays);

        return weekdays;
    }

    private void createSpnDate() {
        List<String> dates = getDates();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dates);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainRecyclerViewAdapter.setTimeIndex(position * 2);
                recyclerViewWeather.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "NothingSelected");
            }
        });
    }

    private void createRecyclerViewWeather() {
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerViewAdapter = new MainRecyclerViewAdapter(this, this.locations);
        mainRecyclerViewAdapter.setClickListener(this);
        recyclerViewWeather.setAdapter(mainRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewWeather.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewWeather.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onLocationClick(View view, int position) {

    }
}

