package edu.ntub.weather.network;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import edu.ntub.weather.dto.Data;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public final class RetrofitConfig {
    private static final String BASE_URL = ApiConfig.API_URL;

    // Create a very simple REST adapter which points the GitHub API.
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public interface API {
        @GET("/data.json")
        Call<Data> data();
    }

    @Nullable
    public static Data getWeather() {
        try {
            // Create an instance of our GitHub API interface.
            API api = retrofit.create(API.class);

            // Create a call instance for looking up Retrofit Datas.
            Call<Data> call = api.data();

            // Fetch and print a list of the Datas to the library.
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
