package edu.ntub.weather.network;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.ntub.weather.helper.JsonHelper;

public class URLConnectionConfig {
    private static final String BASE_URL = ApiConfig.API_URL;

    @Nullable
    public static <T> T get(String apiUrl, Class<T> returnType) {
        try {
            URL url = new URL(BASE_URL + apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            return JsonHelper.fromJsonFile(inputStream, returnType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
