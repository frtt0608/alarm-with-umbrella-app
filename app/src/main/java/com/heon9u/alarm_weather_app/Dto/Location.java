package com.heon9u.alarm_weather_app.Dto;

import java.io.Serializable;

public class Location implements Serializable {
    private int id;
    private int orderNum;
    private String streetAddress;
    private String lotAddress;
    private String communityCenter;
    private Double latitude;
    private Double longitude;

    public void Location() {
        this.id = 0;
    }

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

    @Override
    public String toString() {
        return "Location [id=" + id + ", streetAddress=" + streetAddress + ", lotAddress=" + lotAddress
                + ", communityCenter=" + communityCenter + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }
}
