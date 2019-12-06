package edu.ntub.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ntub.weather.dto.Location;
import edu.ntub.weather.dto.Time;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private LocationClickListener locationClickListener;
    private Context context;
    private List<Location> locations;
    private int timeIndex = 0;

    MainRecyclerViewAdapter(Context context, List<Location> locations) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.ViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.bindWeather(location.locationName, location.weatherElements.get(0).times.get(timeIndex));
        holder.bindHighTemperature(location.weatherElements.get(1).times.get(timeIndex));
        holder.bindLowTemperature(location.weatherElements.get(2).times.get(timeIndex));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    void setTimeIndex(int timeIndex) {
        this.timeIndex = timeIndex;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgWeather;
        TextView txtCity;
        TextView txtHighTemperature;
        TextView txtLowTemperature;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWeather = itemView.findViewById(R.id.imgWeather);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtHighTemperature = itemView.findViewById(R.id.txtHighTemperature);
            txtLowTemperature = itemView.findViewById(R.id.txtLowTemperature);
            itemView.setOnClickListener(this);
        }

        void bindWeather(String info, Time time) {
            txtCity.setText(info);

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

        @Override
        public void onClick(View view) {
            if (locationClickListener != null) {
                locationClickListener.onLocationClick(view, getAdapterPosition());
            }
        }
    }

    Location getLocation(int id) {
        return locations.get(id);
    }

    void setClickListener(LocationClickListener locationClickListener) {
        this.locationClickListener = locationClickListener;
    }

    public interface LocationClickListener {
        void onLocationClick(View view, int position);
    }
}
