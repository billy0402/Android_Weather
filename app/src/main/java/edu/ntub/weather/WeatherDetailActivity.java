package edu.ntub.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntub.weather.dto.Data;
import edu.ntub.weather.dto.Location;
import edu.ntub.weather.dto.Parameter;
import edu.ntub.weather.dto.Point;
import edu.ntub.weather.dto.Time;
import edu.ntub.weather.dto.Weather;
import edu.ntub.weather.dto.WeatherElement;
import edu.ntub.weather.helper.AxisHelper;
import edu.ntub.weather.helper.DataHelper;
import edu.ntub.weather.helper.LegendHelper;
import edu.ntub.weather.network.RetrofitConfig;
import edu.ntub.weather.set.ColorSet;

public class WeatherDetailActivity extends AppCompatActivity {
    private final String TAG = "WeatherDetailActivity";
    private TextView txtCity;
    private RecyclerView recyclerViewWeatherDetail;
    private WeatherDetailRecyclerViewAdapter weatherDetailRecyclerViewAdapter;
    private LineChart lineChart;
    private List<Location> locations;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        txtCity = findViewById(R.id.txtCity);
        recyclerViewWeatherDetail = findViewById(R.id.recyclerViewWeather);
        lineChart = findViewById(R.id.lineChartWeather);

        getLocations();
        int locationIndex = getIntent().getIntExtra("locationIndex", 0);
        location = locations.get(locationIndex);

        txtCity.setText(location.locationName);
        createRecyclerViewWeatherDetail();
        chartWeatherHandler();
    }

    private void getLocations() {
        Thread thread = new Thread(() -> {
            Data data = RetrofitConfig.getWeather();
            Weather weather;

            if (data != null) {
                weather = data.content;
            } else {
                weather = DataHelper.getWeather(WeatherDetailActivity.this, "data");
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

    private void createRecyclerViewWeatherDetail() {
        recyclerViewWeatherDetail.setLayoutManager(new LinearLayoutManager(this));
        weatherDetailRecyclerViewAdapter = new WeatherDetailRecyclerViewAdapter(this, this.location);
        recyclerViewWeatherDetail.setAdapter(weatherDetailRecyclerViewAdapter);
    }

    private void chartWeatherHandler() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(true);
        lineChart.setDrawBorders(true);
        lineChart.setTouchEnabled(true);
        lineChart.setExtraTopOffset(30);
        lineChart.setExtraBottomOffset(20);

        AxisHelper.create(lineChart)
                .leftYAxisGridLineColor(ColorSet.GRAY)
                .position(XAxis.XAxisPosition.BOTTOM)
                .rightYAxisGridLineColor(ColorSet.GRAY)
                .textSize(14)
                .xAxisDrawGridEnable(false);

        LegendHelper.create(lineChart)
                .form(Legend.LegendForm.LINE)
                .formLineWidth(10)
                .horizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
                .isDrawInside(false)
                .orientation(Legend.LegendOrientation.HORIZONTAL)
                .textSize(18)
                .verticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);

        Map<String, List<Entry>> entriesMap = new HashMap<>();
        SparseArray<Point> pointMap = new SparseArray<>();
        List<WeatherElement> weatherElements = location.weatherElements;
        List<String> xIndexList = new ArrayList<>();

        for (int k = 0; k < weatherElements.size(); k = k + 1) {
            WeatherElement weatherElement = weatherElements.get(k);
            List<Entry> entries = new ArrayList<>();
            List<Time> times = weatherElement.times;

            for (int i = 0; i < times.size(); i++) {
                Time time = times.get(i);
                time.parameter.setElementName(weatherElement.name);
                Point point = pointMap.get(i, new Point());
                point.setParameter(weatherElement.name, time.parameter);
                LocalDateTime localDateTime = time.getMiddleLocalDateTime();
                String xTitle = localDateTime.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
                int xIndex = xIndexList.indexOf(xTitle);

                if (xIndex == -1) {
                    xIndexList.add(xTitle);
                    xIndex = xIndexList.size() - 1;
                }

                Parameter parameter = time.parameter;
                String value = parameter.getValue();
                pointMap.put(i, point);
                entries.add(new Entry(xIndex, Float.valueOf(value), time));
            }

            entriesMap.put(weatherElement.name, entries);
        }

        LineDataSet maxTemperatureDataSet = new LineDataSet(entriesMap.get("MaxT"), "最高溫");
        maxTemperatureDataSet.setColor(Color.rgb(245, 177, 176));
        maxTemperatureDataSet.setCircleColor(Color.rgb(245, 177, 176));
        maxTemperatureDataSet.setDrawCircleHole(false);
        maxTemperatureDataSet.setLineWidth(2);
        maxTemperatureDataSet.setValueTextSize(16);
        maxTemperatureDataSet.setValueTextColor(Color.rgb(245, 177, 176));
        maxTemperatureDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        LineDataSet minTemperatureDataSet = new LineDataSet(entriesMap.get("MinT"), "最低溫");
        minTemperatureDataSet.setColor(Color.rgb(132, 170, 208));
        minTemperatureDataSet.setCircleColor(Color.rgb(132, 170, 208));
        minTemperatureDataSet.setDrawCircleHole(false);
        minTemperatureDataSet.setLineWidth(2);
        minTemperatureDataSet.setValueTextSize(16);
        minTemperatureDataSet.setValueTextColor(Color.rgb(132, 170, 208));
        minTemperatureDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        LineData lineData = new LineData(maxTemperatureDataSet, minTemperatureDataSet);
        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(2);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.d(TAG, "ValueSelected");
            }

            @Override
            public void onNothingSelected() {
                Log.d(TAG, "NothingSelected");
            }
        });

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(2, false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return index < 15 ? xIndexList.get(index) : String.valueOf(value);
            }
        });
    }
}
