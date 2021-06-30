package com.heon9u.alarm_weather_app.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "location_table")
public class Location implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int orderNum;
    private String streetAddress;
    private String lotAddress;
    private String communityCenter;
    private Double latitude;
    private Double longitude;
    private Double tmX;
    private Double tmY;

    public void Location() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOrderNum() { return orderNum; }
    public void setOrderNum(int orderNum) { this.orderNum = orderNum; }
    public String getStreetAddress() {
        return streetAddress;
    }
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    public String getLotAddress() {
        return lotAddress;
    }
    public void setLotAddress(String lotAddress) {
        this.lotAddress = lotAddress;
    }
    public String getCommunityCenter() {
        return communityCenter;
    }
    public void setCommunityCenter(String communityCenter) {
        this.communityCenter = communityCenter;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getTmX() {return tmX;}
    public void setTmX(double tmX) {this.tmX = tmX;}
    public Double getTmY() {return tmY;}
    public void setTmY(double tmY) {this.tmY = tmY;}

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", orderNum=" + orderNum +
                ", streetAddress='" + streetAddress + '\'' +
                ", lotAddress='" + lotAddress + '\'' +
                ", communityCenter='" + communityCenter + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", tmX=" + tmX +
                ", tmY=" + tmY +
                '}';
    }
}
