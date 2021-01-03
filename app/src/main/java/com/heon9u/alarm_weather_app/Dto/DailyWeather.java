package com.heon9u.alarm_weather_app.Dto;

import com.heon9u.alarm_weather_app.Dto.Feels_like;
import com.heon9u.alarm_weather_app.Dto.Temperature;
import com.heon9u.alarm_weather_app.Dto.Weather;

/** {"dt":1609642800,
        "sunrise":1609627670,
        "sunset":1609662409,
        "temp":{"day":-3.36,"min":-5.14,"max":-1.9,"night":-3.76,"eve":-3,"morn":-4.94},
        "feels_like":{"day":-8.89,"night":-8.33,"eve":-9.04,"morn":-10.39},
        "pressure":1034,
        "humidity":77,
        "dew_point":-14.72,
        "wind_speed":3.91,
        "wind_deg":325,
        "weather":[{"id":800,"main":"Clear","description":"맑음","icon":"01d"}],
        "clouds":2,
        "pop":0.34,
        "uvi":1.81} **/

public class DailyWeather {
    private int dt;
    private int sunrise;
    private int sunset;

    private Temperature temp;
    private Feels_like feels_like;

    private int pressure;
    private int humidity;
    private float dew_point;
    private float wind_speed;
    private int wind_deg;
    private Weather weather;
    private int clouds;
    private float pop;
    private float uvi;

    public int getDt() {
        return dt;
    }
    public void setDt(int dt) {
        this.dt = dt;
    }
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
    public Temperature getTemp() {
        return temp;
    }
    public void setTemp(Temperature temp) {
        this.temp = temp;
    }
    public Feels_like getFeels_like() {
        return feels_like;
    }
    public void setFeels_like(Feels_like feels_like) {
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
    public float getDew_point() {
        return dew_point;
    }
    public void setDew_point(float dew_point) {
        this.dew_point = dew_point;
    }
    public float getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(float wind_speed) {
        this.wind_speed = wind_speed;
    }
    public int getWind_deg() {
        return wind_deg;
    }
    public void setWind_deg(int wind_deg) {
        this.wind_deg = wind_deg;
    }
    public Weather getWeather() {
        return weather;
    }
    public void setWeather(Weather weather) {
        this.weather = weather;
    }
    public int getClouds() {
        return clouds;
    }
    public void setClouds(int clouds) {
        this.clouds = clouds;
    }
    public float getPop() {
        return pop;
    }
    public void setPop(float pop) {
        this.pop = pop;
    }
    public float getUvi() {
        return uvi;
    }
    public void setUvi(float uvi) {
        this.uvi = uvi;
    }

    @Override
    public String toString() {
        return "DailyWeather [dt=" + dt + ", sunrise=" + sunrise + ", sunset=" + sunset + ", temp=" + temp
                + ", feels_like=" + feels_like + ", pressure=" + pressure + ", humidity=" + humidity + ", dew_point="
                + dew_point + ", wind_speed=" + wind_speed + ", wind_deg=" + wind_deg + ", weather=" + weather
                + ", clouds=" + clouds + ", pop=" + pop + ", uvi=" + uvi + "]";
    }
}
