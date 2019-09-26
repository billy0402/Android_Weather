package edu.ntub.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
        lineChart.setDragEnabled(true);
        lineChart.setDrawGridBackground(true);
        lineChart.setDrawBorders(true);
        lineChart.setTouchEnabled(true);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.d(TAG, "click point");
                Log.d(TAG, entry.toString());
            }

            @Override
            public void onNothingSelected() {
                Log.d(TAG, "NothingSelected");
            }
        });

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setGridColor(Color.BLACK);
        xAxis.setGridLineWidth(2);

        YAxis leftY = lineChart.getAxisLeft();
        leftY.setTextSize(14);
        leftY.setGridColor(Color.BLACK);
        leftY.setGridLineWidth(2);
        YAxis rightY = lineChart.getAxisRight();
        rightY.setTextSize(14);
        rightY.setGridColor(Color.BLACK);
        rightY.setGridLineWidth(2);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(18);
        legend.setFormLineWidth(10);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        Weather weather = DataHelper.getWeather(MainActivity.this, "data");
        DataSet weatherDataSet = weather.dataSet;
        DataSetInfo dataSetInfo = weatherDataSet.dataSetInfo;
        String title = dataSetInfo.description;
        Description description = lineChart.getDescription();
        description.setText(title);
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
                String xTitle = localDateTime.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
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
        maxTemperatureDataSet.setDrawCircleHole(false);
        maxTemperatureDataSet.setLineWidth(2);
        maxTemperatureDataSet.setValueTextSize(14);
        LineDataSet minTemperatureDataSet = new LineDataSet(entriesMap.get("MinT"), "最低溫");
        minTemperatureDataSet.setColor(Color.BLUE);
        minTemperatureDataSet.setCircleColor(Color.BLUE);
        minTemperatureDataSet.setDrawCircleHole(false);
        minTemperatureDataSet.setLineWidth(2);
        minTemperatureDataSet.setValueTextSize(14);
        LineData lineData = new LineData(maxTemperatureDataSet, minTemperatureDataSet);
        lineChart.setData(lineData);
//        xAxis.setLabelCount(5, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
//                int index = (int) value;
//                return xIndexList.get(index);
                return String.valueOf(value);
            }
        });
        lineChart.setVisibleXRangeMaximum(4);
    }
//    }
}
