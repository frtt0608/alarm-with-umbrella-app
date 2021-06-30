package com.heon9u.alarm_weather_app.dto;

import java.io.Serializable;

/** {"dt":1609649554,
        "sunrise":1609627670,
        "sunset":1609662409,
        "temp":-2,
        "feels_like":-7.73,
        "pressure":1034,
        "humidity":25,
        "dew_point":-17.62,
        "uvi":1.34,
        "clouds":75,
        "visibility":10000,
        "wind_speed":3.1,
        "wind_deg":340,
        "weather":[{"id":803,"main":"Clouds","description":"튼구름","icon":"04d"}} **/

public class CurrentWeather implements Serializable {
    private int dt;
    private String date;
    private int sunrise;
    private int sunset;
    private double temp;
    private double feels_like;
    private int pressure;
    private int humidity;
    private double dew_point;
    private double uvi;
    private int clouds;
    private int visibility;
    private double wind_speed;
    private double wind_gust;
    private int wind_deg;
    private double rain1h;
    private double snow1h;

    private Weather weather;

    public int getDt() {
        return dt;
    }
    public void setDt(int dt) {
        this.dt = dt;
    }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getSunrise() {
        return sunrise;
    }
    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }
    public int getSunset() {
        return sunset;
    }
    public void setSunset(int sunset) {
        this.sunset = sunset;
    }
    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }
    public double getFeels_like() {
        return feels_like;
    }
    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }
    public int getPressure() {
        return pressure;
    }
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public double getDew_point() {
        return dew_point;
    }
    public void setDew_point(double dew_point) {
        this.dew_point = dew_point;
    }
    public int getClouds() {
        return clouds;
    }
    public void setClouds(int clouds) {
        this.clouds = clouds;
    }
    public double getUvi() {
        return uvi;
    }
    public void setUvi(double uvi) {
        this.uvi = uvi;
    }
    public int getVisibility() {
        return visibility;
    }
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
    public double getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }
    public double getWind_gust() {
        return wind_gust;
    }
    public void setWind_gust(double wind_gust) {
        this.wind_gust = wind_gust;
    }
    public int getWind_deg() {
        return wind_deg;
    }
    public void setWind_deg(int wind_deg) {
        this.wind_deg = wind_deg;
    }
    public double getRain1h() {
        return rain1h;
    }
    public void setRain1h(double rain1h) {
        this.rain1h = rain1h;
    }
    public double getSnow1h() {
        return snow1h;
    }
    public void setSnow1h(double snow1h) {
        this.snow1h = snow1h;
    }

    public Weather getWeather() {
        return weather;
    }
    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "CurrentWeather [dt=" + dt + ", sunrise=" + sunrise + ", sunset=" + sunset + ", temp=" + temp
                + ", feels_like=" + feels_like + ", pressure=" + pressure + ", humidity=" + humidity + ", dew_point="
                + dew_point + ", clouds=" + clouds + ", uvi=" + uvi + ", visibility=" + visibility + ", wind_speed="
                + wind_speed + ", wind_gust=" + wind_gust + ", wind_deg=" + wind_deg + ", rain1h=" + rain1h
                + ", snow1h=" + snow1h + ", weather=" + weather + "]";
    }
}
