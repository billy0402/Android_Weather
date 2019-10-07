package edu.ntub.weather.dto;

public class Point {
    public Parameter weather;
    public Parameter maxTemperature;
    public Parameter minTemperature;

    public void setParameter(String weatherElementName, Parameter parameter) {
        switch (weatherElementName) {
            case "Wx":
                weather = parameter;
                break;
            case "MaxT":
                maxTemperature = parameter;
                break;
            case "MinT":
                minTemperature = parameter;
                break;
        }
    }
}
