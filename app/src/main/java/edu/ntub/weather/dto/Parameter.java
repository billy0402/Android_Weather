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
    private String elementName;

    public Temperature convertToTemperature() {
        return TemperatureHelper.get(value, Float.valueOf(value));
    }

    public String getName() {
        return elementName.equalsIgnoreCase("Wx") ? name : value;
    }

    public String getValue() {
        return elementName.equalsIgnoreCase("Wx") ? value : name;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
