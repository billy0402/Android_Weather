package edu.ntub.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.ntub.weather.dto.Location
import edu.ntub.weather.dto.Time
import kotlinx.android.synthetic.main.recycler_view_row_main.view.*


class Main2RecyclerViewAdapter(val context: Context, private var locationList: List<Location>) : RecyclerView.Adapter<Main2RecyclerViewAdapter.ViewHolder>() {
    private var timeIndex: Int = 0
    private var listener: OnLocationClickListener? = null

    // 入口
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 指定 layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row_main, parent, false)
        return ViewHolder(view)
    }

    // 綁定資料
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locationList[position]
        if (listener != null) {
            holder.itemView.setOnClickListener { listener!!.onClick(location) }
        }
        holder.bindWeather(location.locationName, location.weatherElements[0].times[timeIndex])
        holder.bindHighTemperature(location.weatherElements[1].times[timeIndex])
        holder.bindLowTemperature(location.weatherElements[2].times[timeIndex])
    }

    // 返回數目
    override fun getItemCount(): Int {
        return locationList.size
    }

    // view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindWeather(locationName: String, time: Time) {
            itemView.txtCity.text = locationName

            time.parameter.elementName = "Wx"
            val weatherImageId = time.parameter.value
            val resourceWeatherImageId = context.resources.getIdentifier(
                    "ic_weather_$weatherImageId",
                    "drawable",
                    context.packageName
            )
            itemView.imgWeather.setImageResource(resourceWeatherImageId)
        }

        fun bindHighTemperature(highTemperature: Time) {
            highTemperature.parameter.elementName = "MaxT"
            itemView.txtHighTemperature.text = highTemperature.parameter.value
        }

        fun bindLowTemperature(lowTemperature: Time) {
            lowTemperature.parameter.elementName = "MinT"
            itemView.txtLowTemperature.text = lowTemperature.parameter.value
        }
    }

    fun updateData(locationList: List<Location>) {
        this.locationList = locationList
    }

    fun setTimeIndex(timeIndex: Int) {
        this.timeIndex = timeIndex
    }

    fun setListener(listener: OnLocationClickListener) {
        this.listener = listener
    }

    @FunctionalInterface
    interface OnLocationClickListener {
        fun onClick(location: Location)
    }
}
