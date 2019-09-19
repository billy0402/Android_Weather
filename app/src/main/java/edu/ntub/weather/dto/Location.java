package edu.ntub.weather.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Location {
    public String locationName;
    @SerializedName("weatherElement")
    public List<WeatherElement> weatherElements;
}
