package com.heon9u.alarm_weather_app.dto;

public class Temperature {
    private double morn;
    private double day;
    private double eve;
    private double night;
    private double min;
    private double max;

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
    public double getMin() {
        return min;
    }
    public void setMin(double min) {
        this.min = min;
    }
    public double getMax() {
        return max;
    }
    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Temperature [morn=" + morn + ", day=" + day + ", eve=" + eve + ", night=" + night + ", min=" + min
                + ", max=" + max + "]";
    }
}
