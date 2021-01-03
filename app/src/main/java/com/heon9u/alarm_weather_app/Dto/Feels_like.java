package com.heon9u.alarm_weather_app.Dto;

public class Feels_like {
    private float morn;
    private float day;
    private float eve;
    private float night;

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

    @Override
    public String toString() {
        return "Feels_like [morn=" + morn + ", day=" + day + ", eve=" + eve + ", night=" + night + "]";
    }
}
