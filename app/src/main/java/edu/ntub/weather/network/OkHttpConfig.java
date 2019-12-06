package edu.ntub.weather.network;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

import edu.ntub.weather.helper.JsonHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpConfig {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = ApiConfig.API_URL;

    @Nullable
    public static <T> T get(String apiUrl, Class<T> returnType) {
        Request request = new Request.Builder()
                .url(BASE_URL + apiUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            InputStream inputStream = response.body().byteStream();

            return JsonHelper.fromJsonFile(inputStream, returnType);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
