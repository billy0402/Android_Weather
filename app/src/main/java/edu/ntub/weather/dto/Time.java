package edu.ntub.weather.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Time {
    private static final int START_TIME_AND_END_TIME_DIFFERENCE_DIVISION_TWO = 6;

    public String startTime;
    public String endTime;
    public Parameter parameter;

    public LocalDateTime getMiddleLocalDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(startTime);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        return localDateTime.plusHours(START_TIME_AND_END_TIME_DIFFERENCE_DIVISION_TWO);
    }
}
