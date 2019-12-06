package edu.ntub.weather.helper;

import android.content.Context;

import java.io.InputStream;

import edu.ntub.weather.dto.Data;
import edu.ntub.weather.dto.Weather;

public class DataHelper {
    private DataHelper() {

    }

    public static Weather getWeather(Context context, String fileName) {
        InputStream inputStream = ResourceHelper.getRawFileInputStream(context, fileName);
        Data data = JsonHelper.fromJsonFile(inputStream, Data.class);
        return data.content;
    }
}
