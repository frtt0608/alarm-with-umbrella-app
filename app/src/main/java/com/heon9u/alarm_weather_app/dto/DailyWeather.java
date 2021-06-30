package com.heon9u.alarm_weather_app.dto;

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
    private String date;
    private int sunrise;
    private int sunset;
    private int pressure;
    private int humidity;
    private double dew_point;
    private double wind_speed;
    private int wind_deg;
    private int clouds;
    private double pop;
    private double uvi;
    private double rain;
    private double snow;

    private Temperature temp;
    private Feels_like feels_like;
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
    public double getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }
    public int getWind_deg() {
        return wind_deg;
    }
    public void setWind_deg(int wind_deg) {
        this.wind_deg = wind_deg;
    }
    public int getClouds() {
        return clouds;
    }
    public void setClouds(int clouds) {
        this.clouds = clouds;
    }
    public double getPop() {
        return pop;
    }
    public void setPop(double pop) {
        this.pop = pop;
    }
    public double getUvi() {
        return uvi;
    }
    public void setUvi(double uvi) {
        this.uvi = uvi;
    }
    public double getRain() {
        return rain;
    }
    public void setRain(double rain) {
        this.rain = rain;
    }
    public double getSnow() {
        return snow;
    }
    public void setSnow(double snow) {
        this.snow = snow;
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
    public Weather getWeather() {
        return weather;
    }
    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "DailyWeather [dt=" + dt + ", sunrise=" + sunrise + ", sunset=" + sunset + ", temp=" + temp
                + ", feels_like=" + feels_like + ", pressure=" + pressure + ", humidity=" + humidity + ", dew_point="
                + dew_point + ", wind_speed=" + wind_speed + ", wind_deg=" + wind_deg + ", weather=" + weather
                + ", clouds=" + clouds + ", pop=" + pop + ", uvi=" + uvi + "]";
    }
}
