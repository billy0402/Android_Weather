package edu.ntub.weather.helper;

import edu.ntub.weather.dto.Temperature;

public class TemperatureHelper {
    private TemperatureHelper() {

    }

    public static Temperature get(String name, float temperature) {
        return new Temperature(Temperature.Type.fromName(name), temperature);
    }

    public static float toCelsius(float fah, int scale) {
        return FloatHelper.round(toCelsius(fah), scale);
    }

    public static float toCelsius(float fah) {
        return (fah - 32) * 5 / 9;
    }

    public static float toFah(float celsius, int scale) {
        return FloatHelper.round(toFah(celsius), scale);
    }

    public static float toFah(float celsius) {
        return celsius * (9 / 5) + 32;
    }
}
