package com.heon9u.alarm_weather_app.dto;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "alarm_table")
public class Alarm implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int hour;
    private int minute;
    private String title;
    private boolean totalFlag = true;
    private boolean allDayFlag;
    private String day = "";

    private int volume;
    private boolean basicSoundFlag;
    private String basicSoundTitle = "기본음";
    private String basicSoundUri = "content://settings/system/ringtone";
    private boolean umbSoundFlag;
    private String umbSoundTitle = "기본음";
    private String umbSoundUri = "content://settings/system/ringtone";
    private boolean vibFlag;

    private int location_id = 0;

    public Alarm() {}

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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isBasicSoundFlag() {
        return basicSoundFlag;
    }


    public void setBasicSoundFlag(boolean basicSoundFlag) {
        this.basicSoundFlag = basicSoundFlag;
    }

    public String getBasicSoundTitle() {
        return basicSoundTitle;
    }

    public void setBasicSoundTitle(String basicSoundTitle) {
        this.basicSoundTitle = basicSoundTitle;
    }

    public String getBasicSoundUri() {
        return basicSoundUri;
    }

    public void setBasicSoundUri(String basicSoundUri) {
        this.basicSoundUri = basicSoundUri;
    }

    public boolean isUmbSoundFlag() {
        return umbSoundFlag;
    }

    public void setUmbSoundFlag(boolean umbSoundFlag) {
        this.umbSoundFlag = umbSoundFlag;
    }

    public String getUmbSoundTitle() {
        return umbSoundTitle;
    }

    public void setUmbSoundTitle(String umbSoundTitle) {
        this.umbSoundTitle = umbSoundTitle;
    }

    public String getUmbSoundUri() {
        return umbSoundUri;
    }

    public void setUmbSoundUri(String umbSoundUri) {
        this.umbSoundUri = umbSoundUri;
    }

    public boolean isVibFlag() {
        return vibFlag;
    }

    public void setVibFlag(boolean vibFlag) {
        this.vibFlag = vibFlag;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", hour=" + hour +
                ", minute=" + minute +
                ", title='" + title + '\'' +
                ", totalFlag=" + totalFlag +
                ", allDayFlag=" + allDayFlag +
                ", day='" + day + '\'' +
                ", volume=" + volume +
                ", basicSoundFlag=" + basicSoundFlag +
                ", basicSoundTitle='" + basicSoundTitle + '\'' +
                ", basicSoundUri='" + basicSoundUri + '\'' +
                ", umbSoundFlag=" + umbSoundFlag +
                ", umbSoundTitle='" + umbSoundTitle + '\'' +
                ", umbSoundUri='" + umbSoundUri + '\'' +
                ", vibFlag=" + vibFlag +
                ", location_id=" + location_id +
                '}';
    }
}
