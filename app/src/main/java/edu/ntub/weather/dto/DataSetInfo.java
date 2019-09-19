package edu.ntub.weather.dto;

import com.google.gson.annotations.SerializedName;

public class DataSetInfo {
    @SerializedName("datasetDescription")
    public String description;
    public String issueTime;
    public String update;
}
