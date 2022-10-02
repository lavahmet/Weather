package com.example.weather.util;

public class MeasurementErrorResponse {
    private String message;
    private long timestap;

    public MeasurementErrorResponse(String message, long timestap) {
        this.message = message;
        this.timestap = timestap;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestap() {
        return timestap;
    }

    public void setTimestap(long timestap) {
        this.timestap = timestap;
    }
}
