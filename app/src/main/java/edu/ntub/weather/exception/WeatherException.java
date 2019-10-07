package edu.ntub.weather.exception;

public class WeatherException extends Exception {
    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(Throwable cause) {
        super(cause);
    }
}
