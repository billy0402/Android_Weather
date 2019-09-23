package edu.ntub.weather;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntub.weather.dto.DataSet;
import edu.ntub.weather.dto.DataSetInfo;
import edu.ntub.weather.dto.Location;
import edu.ntub.weather.dto.Parameter;
import edu.ntub.weather.dto.Time;
import edu.ntub.weather.dto.Weather;
import edu.ntub.weather.dto.WeatherElement;
import edu.ntub.weather.helper.DataHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineChart lineChart = findViewById(R.id.main_chart);
        lineChart.setDrawGridBackground(true);
        lineChart.setDrawBorders(true);
        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        Weather weather = DataHelper.getWeather(MainActivity.this, "data");
        DataSet weatherDataSet = weather.dataSet;
        DataSetInfo dataSetInfo = weatherDataSet.dataSetInfo;
        String title = dataSetInfo.description;
        List<Location> locations = weatherDataSet.locations;
//        for (Location location : locations) {
        Map<String, List<Entry>> entriesMap = new HashMap<>();
        List<WeatherElement> weatherElements = locations.get(0).weatherElements;
        String locationName = locations.get(0).locationName;
        List<String> xIndexList = new ArrayList<>();
        for (int k = 0; k < weatherElements.size(); k = k + 1) {
            WeatherElement weatherElement = weatherElements.get(k);
            List<Entry> entries = new ArrayList<>();
            for (Time time : weatherElement.times) {
                LocalDateTime localDateTime = time.getMiddleLocalDateTime();
                String xTitle = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                int xIndex = xIndexList.indexOf(xTitle);
                if (xIndex == -1) {
                    xIndexList.add(xTitle);
                    xIndex = xIndexList.size() - 1;
                }
                Parameter parameter = time.parameter;
                String value = parameter.getValue(weatherElement.name);
                entries.add(new Entry(xIndex, Float.valueOf(value), time));
            }
            entriesMap.put(weatherElement.name, entries);
        }
        LineDataSet maxTemperatureDataSet = new LineDataSet(entriesMap.get("MaxT"), "最高溫");
        maxTemperatureDataSet.setColor(Color.RED);
        maxTemperatureDataSet.setCircleColor(Color.RED);
//        maxTemperatureDataSet.setLineWidth(1f);
//        maxTemperatureDataSet.setCircleRadius(3f);
        maxTemperatureDataSet.setDrawCircleHole(false);
        LineDataSet minTemperatureDataSet = new LineDataSet(entriesMap.get("MinT"), "最低溫");
        minTemperatureDataSet.setColor(Color.BLUE);
        minTemperatureDataSet.setCircleColor(Color.BLUE);
//        minTemperatureDataSet.setLineWidth(1f);
//        minTemperatureDataSet.setCircleRadius(3f);
        minTemperatureDataSet.setDrawCircleHole(false);
        LineData lineData = new LineData(maxTemperatureDataSet, minTemperatureDataSet);
        lineChart.setData(lineData);
    }
//    }
}
