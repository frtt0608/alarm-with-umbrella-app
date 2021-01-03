package com.heon9u.alarm_weather_app.Dto;

public class Temperature {
    private float morn;
    private float day;
    private float eve;
    private float night;
    private float min;
    private float max;

    public float getMorn() {
        return morn;
    }
    public void setMorn(float morn) {
        this.morn = morn;
    }
    public float getDay() {
        return day;
    }
    public void setDay(float day) {
        this.day = day;
    }
    public float getEve() {
        return eve;
    }
    public void setEve(float eve) {
        this.eve = eve;
    }
    public float getNight() {
        return night;
    }
    public void setNight(float night) {
        this.night = night;
    }
    public float getMin() {
        return min;
    }
    public void setMin(float min) {
        this.min = min;
    }
    public float getMax() {
        return max;
    }
    public void setMax(float max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Temperature [morn=" + morn + ", day=" + day + ", eve=" + eve + ", night=" + night + ", min=" + min
                + ", max=" + max + "]";
    }
}
