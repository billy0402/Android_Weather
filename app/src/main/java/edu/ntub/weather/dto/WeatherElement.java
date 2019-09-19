package edu.ntub.weather.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherElement {
    @SerializedName("elementName")
    public String name;
    @SerializedName("time")
    public List<Time> times;
}
