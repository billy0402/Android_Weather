package edu.ntub.weather.helper;

import android.content.Context;

import edu.ntub.weather.dto.Data;
import edu.ntub.weather.dto.Weather;

public class DataHelper {
    private DataHelper() {

    }

    public static Weather getWeather(Context context) {
        Data data = JsonHelper.fromRawDirectoryJsonFile(context, "data", Data.class);
        return data.content;
    }
}
