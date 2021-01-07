package com.heon9u.alarm_weather_app.Dto;

import java.io.Serializable;

public class Alarm implements Serializable {
    private int id;
    private int hour;
    private int minute;
    private String title;
    private boolean totalFlag;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean getTotalFlag() { return totalFlag; }
    public void setTotalFlag(boolean totalFlag) { this.totalFlag = totalFlag; }

    @Override
    public String toString() {
        return "alarm [id=" + id + ", hour=" + hour + ", minute=" + minute + ", title=" + title + "]";
    }
}
