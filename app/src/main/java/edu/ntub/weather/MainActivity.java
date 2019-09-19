package edu.ntub.weather;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.InputStreamReader;

import edu.ntub.weather.dto.Data;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        int rID = getResources().getIdentifier(getPackageName() + ":raw/data", null, null);
        Data data = gson.fromJson(new InputStreamReader(getResources().openRawResource(rID)), Data.class);
    }
}
