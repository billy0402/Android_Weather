package edu.ntub.weather.dto;

import com.google.gson.annotations.SerializedName;

public class Parameter {
    /**
     * parameterName : 晴時多雲
     * parameterValue : 2
     */
    @SerializedName("parameterName")
    public String name;
    @SerializedName("parameterValue")
    public String value;
}
