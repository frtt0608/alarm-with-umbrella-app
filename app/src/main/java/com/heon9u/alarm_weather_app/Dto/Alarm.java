package com.heon9u.alarm_weather_app.Dto;

import java.io.Serializable;

public class Alarm implements Serializable {
    private int id;
    private int hour;
    private int minute;
    private String title;
    private boolean totalFlag;
    private boolean allDayFlag;
    private String day;
    private boolean basicSoundFlag;
    private String basicSound;
    private boolean umbSoundFlag;
    private String umbSound;
    private boolean vibFlag;

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
    public boolean isTotalFlag() {
        return totalFlag;
    }
    public void setTotalFlag(boolean totalFlag) {
        this.totalFlag = totalFlag;
    }
    public boolean isAllDayFlag() {
        return allDayFlag;
    }
    public void setAllDayFlag(boolean allDayFlag) {
        this.allDayFlag = allDayFlag;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getBasicSound() {
        return basicSound;
    }
    public void setBasicSound(String basicSound) {
        this.basicSound = basicSound;
    }
    public boolean isBasicSoundFlag() {
        return basicSoundFlag;
    }
    public void setBasicSoundFlag(boolean basicSoundFlag) {
        this.basicSoundFlag = basicSoundFlag;
    }
    public String getUmbSound() {
        return umbSound;
    }
    public void setUmbSound(String umbSound) {
        this.umbSound = umbSound;
    }
    public boolean isUmbSoundFlag() {
        return umbSoundFlag;
    }
    public void setUmbSoundFlag(boolean umbSoundFlag) {
        this.umbSoundFlag = umbSoundFlag;
    }
    public boolean isVibFlag() {
        return vibFlag;
    }
    public void setVibFlag(boolean vibFlag) {
        this.vibFlag = vibFlag;
    }

    @Override
    public String toString() {
        return "alarm [id=" + id + ", hour=" + hour + ", minute=" + minute + ", title=" + title + ", totalFlag="
                + totalFlag + ", allDayFlag=" + allDayFlag + ", day=" + day + ", basicSound=" + basicSound
                + ", basicSoundFlag=" + basicSoundFlag + ", umbSound=" + umbSound + ", umbSoundFlag=" + umbSoundFlag
                + ", vibFlag=" + vibFlag + "]";
    }
}
