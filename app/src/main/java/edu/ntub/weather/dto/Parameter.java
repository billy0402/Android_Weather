package edu.ntub.weather.dto;

import com.google.gson.annotations.SerializedName;

import edu.ntub.weather.helper.TemperatureHelper;

public class Parameter {
    /**
     * parameterName : 晴時多雲
     * parameterValue : 2
     */
    @SerializedName("parameterName")
    private String name;
    @SerializedName(value = "parameterValue", alternate = {"parameterUnit"})
    private String value;

    public Temperature convertToTemperature() {
        return TemperatureHelper.get(value, Float.valueOf(value));
    }

    public String getName(String elementName) {
        return elementName.equalsIgnoreCase("Wx") ? name : value;
    }

    public String getValue(String elementName) {
        return elementName.equalsIgnoreCase("Wx") ? value : name;
    }
}
