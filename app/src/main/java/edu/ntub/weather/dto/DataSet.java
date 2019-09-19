package edu.ntub.weather.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataSet {
    @SerializedName("datasetInfo")
    public DataSetInfo dataSetInfo;
    @SerializedName("location")
    public List<Location> locations;
}
