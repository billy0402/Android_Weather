package edu.ntub.weather.helper;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonHelper {
    private JsonHelper() {

    }

    public static <T> T fromJsonFile(InputStream inputStream, Class<T> returnType) {
        Gson gson = new Gson();
        InputStreamReader reader = new InputStreamReader(inputStream);
        return gson.fromJson(reader, returnType);
    }
}
