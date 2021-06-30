package com.heon9u.alarm_weather_app.dto;

public class FineDust {
    private String stationName;
    private int pm10Value;
    private int pm25Value;
    private double o3Value;

    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getPm10Value() {
        return this.pm10Value;
    }

    public void setPm10Value(int pm10Value) {
        this.pm10Value = pm10Value;
    }

    public int getPm25Value() {
        return this.pm25Value;
    }

    public void setPm25Value(int pm25Value) {
        this.pm25Value = pm25Value;
    }

    public double getO3Value() {
        return this.o3Value;
    }

    public void setO3Value(double o3Value) {
        this.o3Value = o3Value;
    }
}
