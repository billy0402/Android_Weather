package edu.ntub.weather.helper;

import android.content.Context;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;

import edu.ntub.weather.dto.Data;

public class JsonHelper {
    private JsonHelper() {

    }

    public static <T> T fromRawDirectoryJsonFile(Context context, String fileName, Class<T> returnType) {
        Gson gson = new Gson();
        InputStream inputStream = ResourceHelper.getRawFileInputStream(context, fileName);
        InputStreamReader reader = new InputStreamReader(inputStream);
        return gson.fromJson(reader, returnType);
    }
}
