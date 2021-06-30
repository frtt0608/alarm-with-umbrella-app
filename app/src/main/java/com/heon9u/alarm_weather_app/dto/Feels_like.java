package com.heon9u.alarm_weather_app.dto;

public class Feels_like {
    private double morn;
    private double day;
    private double eve;
    private double night;

    public double getMorn() {
        return morn;
    }
    public void setMorn(double morn) {
        this.morn = morn;
    }
    public double getDay() {
        return day;
    }
    public void setDay(double day) {
        this.day = day;
    }
    public double getEve() {
        return eve;
    }
    public void setEve(double eve) {
        this.eve = eve;
    }
    public double getNight() {
        return night;
    }
    public void setNight(double night) {
        this.night = night;
    }

    @Override
    public String toString() {
        return "Feels_like [morn=" + morn + ", day=" + day + ", eve=" + eve + ", night=" + night + "]";
    }
}
