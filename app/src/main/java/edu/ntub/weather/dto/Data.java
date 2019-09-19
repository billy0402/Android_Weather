package edu.ntub.weather.dto;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("cwbopendata")
    public Weather content;
}
