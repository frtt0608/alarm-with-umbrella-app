package com.heon9u.alarm_weather_app.dto;

public class OpenWeather {

    private String lat;
    private String lon;
    private String timezone;
    private String timezone_offset;

    private CurrentWeather current;
    private HourlyWeather hourly;
    private DailyWeather daily;

    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    public String getTimezone_offset() {
        return timezone_offset;
    }
    public void setTimezone_offset(String timezone_offset) { this.timezone_offset = timezone_offset; }
    public CurrentWeather getCurrent() {
        return current;
    }
    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }
    public HourlyWeather getHourly() {
        return hourly;
    }
    public void setHourly(HourlyWeather hourly) {
        this.hourly = hourly;
    }
    public DailyWeather getDaily() {
        return daily;
    }
    public void setDaily(DailyWeather daily) {
        this.daily = daily;
    }

    @Override
    public String toString() {
        return "OpenWeather [lat=" + lat + ", lon=" + lon + ", timezone=" + timezone + ", timezone_offset="
                + timezone_offset + ", current=" + current + ", hourly=" + hourly + ", daily=" + daily + "]";
    }
}
