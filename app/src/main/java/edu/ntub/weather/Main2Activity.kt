package edu.ntub.weather

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import edu.ntub.weather.dto.Data
import edu.ntub.weather.dto.Location
import edu.ntub.weather.dto.Point
import edu.ntub.weather.dto.Time
import edu.ntub.weather.helper.AxisHelper
import edu.ntub.weather.helper.DataHelper
import edu.ntub.weather.helper.LegendHelper
import edu.ntub.weather.network.RetrofitConfig
import edu.ntub.weather.set.ColorSet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_dialog_weather.view.*
import java.time.format.DateTimeFormatter
import java.util.*

class Main2Activity : AppCompatActivity() {
    private val TAG = "Main2Activity"
    private var locations: List<Location> = emptyList()
    private var selectDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.locations = getLocations()
        createSpnDate()
        createRecyclerViewWeather()
    }

    private fun createSpnDate() {
        // String array
        val dates = getDates()

        // Adapter for spinner
        spnDate.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dates)

        // item selected listener for spinner
        spnDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val adapter = recyclerViewWeather.adapter as Main2RecyclerViewAdapter
                adapter.setTimeIndex(position * 2)
                recyclerViewWeather.adapter?.notifyDataSetChanged()
                selectDate = dates[position].substring(0, 10)
            }
        }
    }

    private fun getLocations(): List<Location> {
        var data: Data? = null
        val networkThread = Thread(Runnable {
            data = RetrofitConfig.getWeather()
        })
        networkThread.start()
        networkThread.join()

        val weather = if (data != null) {
            data!!.content
        } else {
            DataHelper.getWeather(this@Main2Activity, "data")
        }

        return weather.dataSet.locations
    }

    private fun getDates(): List<String> {
        return locations[0].weatherElements[0].times.map {
            Time.getStringFormat(it.middleLocalDateTime, "yyyy-MM-dd E")
        }.distinct().dropLast(1)
    }

    private fun createRecyclerViewWeather() {
        recyclerViewWeather.layoutManager = LinearLayoutManager(this)

        val adapter = Main2RecyclerViewAdapter(this, this.locations)
        adapter.updateData(this.locations)
        adapter.setListener(object : Main2RecyclerViewAdapter.OnLocationClickListener {
            override fun onClick(location: Location) {
                val builder = AlertDialog.Builder(this@Main2Activity)
                val layoutInflater = LayoutInflater.from(this@Main2Activity)
                val view = layoutInflater.inflate(R.layout.alert_dialog_weather, null, false)
                view.txtCity.text = location.locationName
                val weatherLineChart = view.lineChartWeather
                chartWeatherHandler(chart = weatherLineChart, location = location)
                builder.setView(view)
                builder.show()
            }
        })
        recyclerViewWeather.adapter = adapter
        recyclerViewWeather.addItemDecoration(DividerItemDecoration(recyclerViewWeather.context, DividerItemDecoration.VERTICAL))
    }

    private fun chartWeatherHandler(chart: LineChart, location: Location) {
        chart.description.isEnabled = false
        chart.isDragEnabled = false
        chart.isScaleYEnabled = false
        chart.extraTopOffset = 30f
        chart.extraBottomOffset = 20f
        chart.setDrawGridBackground(true)
        chart.setDrawBorders(true)
        chart.setTouchEnabled(true)

        AxisHelper.create(chart)
                .leftYAxisGridLineColor(ColorSet.GRAY)
                .position(XAxis.XAxisPosition.BOTTOM)
                .rightYAxisGridLineColor(ColorSet.GRAY)
                .textSize(14f)
                .xAxisDrawGridEnable(false)

        LegendHelper.create(chart)
                .form(Legend.LegendForm.LINE)
                .formLineWidth(10f)
                .horizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
                .isDrawInside(false)
                .orientation(Legend.LegendOrientation.HORIZONTAL)
                .textSize(18f)
                .verticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)

        val entriesMap = HashMap<String, List<Entry>>()
        val pointMap = SparseArray<Point>()
        val weatherElements = location.weatherElements
        val xIndexList = ArrayList<String>()

        for (weatherElement in weatherElements) {
            val entries = ArrayList<Entry>()
            val times = weatherElement.times

            for ((index, time) in times.withIndex()) {
                if (!(time.startTime.contains(selectDate) || time.endTime.contains(selectDate))) {
                    continue
                }
                time.parameter.elementName = weatherElement.name
                val point = pointMap.get(index, Point())
                point.setParameter(weatherElement.name, time.parameter)
                val localDateTime = time.middleLocalDateTime
                val xTitle = localDateTime.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"))
                var xIndex = xIndexList.indexOf(xTitle)

                if (xIndex == -1) {
                    xIndexList.add(xTitle)
                    xIndex = xIndexList.size - 1
                }

                val parameter = time.parameter
                val value = parameter.value
                pointMap.put(index, point)
                entries.add(Entry(xIndex.toFloat(), java.lang.Float.valueOf(value), time))
            }

            entriesMap[weatherElement.name] = entries
        }

        val maxTemperatureDataSet = LineDataSet(entriesMap["MaxT"], "最高溫")
        maxTemperatureDataSet.color = Color.rgb(245, 177, 176)
        maxTemperatureDataSet.setCircleColor(Color.rgb(245, 177, 176))
        maxTemperatureDataSet.setDrawCircleHole(false)
        maxTemperatureDataSet.lineWidth = 2f
        maxTemperatureDataSet.valueTextSize = 16f
        maxTemperatureDataSet.valueTextColor = Color.rgb(245, 177, 176)
        maxTemperatureDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        val minTemperatureDataSet = LineDataSet(entriesMap["MinT"], "最低溫")
        minTemperatureDataSet.color = Color.rgb(132, 170, 208)
        minTemperatureDataSet.setCircleColor(Color.rgb(132, 170, 208))
        minTemperatureDataSet.setDrawCircleHole(false)
        minTemperatureDataSet.lineWidth = 2f
        minTemperatureDataSet.valueTextSize = 16f
        minTemperatureDataSet.valueTextColor = Color.rgb(132, 170, 208)
        minTemperatureDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        val lineData = LineData(maxTemperatureDataSet, minTemperatureDataSet)
        chart.data = lineData
        chart.setVisibleXRangeMaximum(2f)
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, highlight: Highlight) {
                Log.d(TAG, "ValueSelected")
            }

            override fun onNothingSelected() {
                Log.d(TAG, "NothingSelected")
            }
        })

        val xAxis = chart.xAxis
        xAxis.setLabelCount(2, false)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index < 15) xIndexList[index] else value.toString()
            }
        }
    }
}
