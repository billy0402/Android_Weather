package edu.ntub.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import edu.ntub.weather.dto.DataSet;
import edu.ntub.weather.dto.DataSetInfo;
import edu.ntub.weather.dto.Location;
import edu.ntub.weather.dto.Parameter;
import edu.ntub.weather.dto.Point;
import edu.ntub.weather.dto.Time;
import edu.ntub.weather.dto.Weather;
import edu.ntub.weather.dto.WeatherElement;
import edu.ntub.weather.helper.AxisHelper;
import edu.ntub.weather.helper.DataHelper;
import edu.ntub.weather.helper.LegendHelper;
import edu.ntub.weather.set.ColorSet;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView textWeather;
    private TextView textMaxTemperature;
    private TextView textMinTemperature;
    private Button buttonCelsius;
    private Button buttonFah;
    private LineChart lineChart;

    private boolean isCelsius = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textWeather = findViewById(R.id.txtWeatherContent);
        textMaxTemperature = findViewById(R.id.txtMaxTemperatureContent);
        textMinTemperature = findViewById(R.id.txtMinTemperatureContent);
        buttonCelsius = findViewById(R.id.btnCelsius);
        buttonCelsius.setOnClickListener(view -> {
            isCelsius = true;
            lineChart.invalidate();
        });
        buttonFah = findViewById(R.id.btnFahrenheit);
        buttonFah.setOnClickListener(view -> {
            isCelsius = false;
            lineChart.invalidate();
        });
        lineChart = findViewById(R.id.main_chart);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(true);
        lineChart.setDrawBorders(true);
        lineChart.setTouchEnabled(true);
        lineChart.setExtraTopOffset(30);
        lineChart.setExtraBottomOffset(20);
        AxisHelper.create(lineChart)
                .position(XAxis.XAxisPosition.BOTTOM)
                .textSize(14)
                .xAxisDrawGridEnable(false)
                .leftYAxisGridLineColor(ColorSet.GRAY)
                .rightYAxisGridLineColor(ColorSet.GRAY);
        LegendHelper.create(lineChart)
                .isDrawInside(false)
                .form(Legend.LegendForm.LINE)
                .formLineWidth(10)
                .verticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
                .horizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
                .orientation(Legend.LegendOrientation.HORIZONTAL)
                .textSize(18);

        Weather weather = DataHelper.getWeather(MainActivity.this, "data");
        DataSet weatherDataSet = weather.dataSet;
        DataSetInfo dataSetInfo = weatherDataSet.dataSetInfo;
        String title = dataSetInfo.description;
//        Description description = lineChart.getDescription();
//        description.setText(title);
        List<Location> locations = weatherDataSet.locations;
//        for (Location location : locations) {
        Map<String, List<Entry>> entriesMap = new HashMap<>();
        SparseArray<Point> pointMap = new SparseArray<>();
        List<WeatherElement> weatherElements = locations.get(0).weatherElements;
        String locationName = locations.get(0).locationName;
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
//        xAxis.setLabelCount(5, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return index < 15 ? xIndexList.get(index) : String.valueOf(value);
            }
        });
        xAxis.setLabelCount(2, false);
        lineChart.setVisibleXRangeMaximum(2);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Point point = pointMap.get((int) entry.getX());
                textWeather.setText(point.weather.getName());
                textMaxTemperature.setText(point.maxTemperature.getValue());
                textMinTemperature.setText(point.minTemperature.getValue());
            }

            @Override
            public void onNothingSelected() {
                Log.d(TAG, "NothingSelected");
            }
        });
    }
//    }
}
