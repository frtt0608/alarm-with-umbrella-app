package com.heon9u.alarm_weather_app.dto;

/** {"dt":1609646400,"temp":-2.63,
        "feels_like":-9.4,
        "pressure":1034,
        "humidity":23,
        "dew_point":-19.06,
        "uvi":1.68,
        "clouds":0,
        "visibility":10000,
        "wind_speed":3.93,
        "wind_deg":314,
        "weather":[{"id":800,"main":"Clear","description":"맑음","icon":"01d"}],
        "pop":0.01},**/

public class HourlyWeather {
    private int dt;
    private String date;
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
    private double pop;
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
    public double getUvi() {
        return uvi;
    }
    public void setUvi(double uvi) {
        this.uvi = uvi;
    }
    public int getClouds() {
        return clouds;
    }
    public void setClouds(int clouds) {
        this.clouds = clouds;
    }
    public int getVisibility() {
        return visibility;
    }
    public void setVisibility(int visibility) { this.visibility = visibility; }
    public double getWind_speed() { return wind_speed; }
    public void setWind_speed(double wind_speed) { this.wind_speed = wind_speed; }
    public double getWind_gust() { return wind_gust; }
    public void setWind_gust(double wind_gust) { this.wind_gust = wind_gust; }
    public int getWind_deg() { return wind_deg; }
    public void setWind_deg(int wind_deg) { this.wind_deg = wind_deg; }
    public double getPop() { return pop; }
    public void setPop(double pop) { this.pop = pop; }
    public double getRain1h() { return rain1h; }
    public void setRain1h(double rain1h) { this.rain1h = rain1h; }
    public double getSnow1h() { return snow1h; }
    public void setSnow1h(double snow1h) { this.snow1h = snow1h; }

    public Weather getWeather() { return weather; }
    public void setWeather(Weather weather) { this.weather = weather; }

    @Override
    public String toString() {
        return "HourlyWeather [dt=" + dt + "date=" + date +
                ", temp=" + temp + ", feels_like=" + feels_like + ", pressure=" + pressure
                + ", humidity=" + humidity + ", dew_point=" + dew_point + ", uvi=" + uvi + ", clouds=" + clouds
                + ", visibility=" + visibility + ", wind_speed=" + wind_speed + ", wind_gust=" + wind_gust
                + ", wind_deg=" + wind_deg + ", pop=" + pop + ", rain1h=" + rain1h + ", snow1h=" + snow1h + ", weather="
                + weather + "]";
    }
}
