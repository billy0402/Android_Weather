package edu.ntub.weather.dto;

import com.google.gson.annotations.SerializedName;

public class Weather {
    public String identifier;
    public String sender;
    public String sent;
    public String status;
    public String msgType;
    public String source;
    @SerializedName("dataid")
    public String dataId;
    public String scope;
    @SerializedName("dataset")
    public DataSet dataSet;
}
