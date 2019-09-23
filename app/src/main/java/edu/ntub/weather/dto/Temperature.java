package edu.ntub.weather.dto;

import edu.ntub.weather.helper.FloatHelper;
import edu.ntub.weather.helper.TemperatureHelper;

public class Temperature {
    private float celsius;
    private float fah;

    public Temperature(Type type, float temperature) {
        switch (type) {
            case CELSIUS:
                celsius = temperature;
                fah = TemperatureHelper.toFah(temperature);
                break;
            case FAH:
                celsius = TemperatureHelper.toCelsius(temperature);
                fah = temperature;
                break;
        }
    }

    public float getCelsius(int scale) {
        return FloatHelper.round(celsius, scale);
    }

    public float getFah(int scale) {
        return FloatHelper.round(fah, scale);
    }

    public static enum Type {
        // 攝氏, 華氏
        CELSIUS, FAH;

        public static Type fromName(String name) {
            return name.equalsIgnoreCase("C") ? CELSIUS : FAH;
        }
    }
}
