package edu.ntub.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ntub.weather.dto.Location;
import edu.ntub.weather.dto.Time;

public class WeatherDetailRecyclerViewAdapter extends RecyclerView.Adapter<WeatherDetailRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private Location location;

    WeatherDetailRecyclerViewAdapter(Context context, Location location) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.location = location;
    }

    @NonNull
    @Override
    public WeatherDetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_weather_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDetailRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindWeather(location.weatherElements.get(0).times.get(position * 2));
        holder.bindHighTemperature(location.weatherElements.get(1).times.get(position * 2));
        holder.bindLowTemperature(location.weatherElements.get(2).times.get(position * 2));
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtWeekday;
        TextView txtHighTemperature;
        TextView txtLowTemperature;
        ImageView imgWeather;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWeekday = itemView.findViewById(R.id.txtWeekday);
            txtHighTemperature = itemView.findViewById(R.id.txtHighTemperature);
            txtLowTemperature = itemView.findViewById(R.id.txtLowTemperature);
            imgWeather = itemView.findViewById(R.id.imgWeather);
        }

        void bindWeather(Time time) {
            String weekday = Time.getStringFormat(time.getMiddleLocalDateTime(), "E");
            txtWeekday.setText(weekday);

            time.parameter.setElementName("Wx");
            String weatherImageId = time.parameter.getValue();
            int resourceWeatherImageId = context.getResources().getIdentifier(
                    "ic_weather_" + weatherImageId,
                    "drawable",
                    context.getPackageName()
            );
            imgWeather.setImageResource(resourceWeatherImageId);
        }

        void bindHighTemperature(Time highTemperature) {
            highTemperature.parameter.setElementName("MaxT");
            txtHighTemperature.setText(highTemperature.parameter.getValue());
        }

        void bindLowTemperature(Time lowTemperature) {
            lowTemperature.parameter.setElementName("MinT");
            txtLowTemperature.setText(lowTemperature.parameter.getValue());
        }
    }
}
