package com.heon9u.alarm_weather_app.Dto;

public class Location {
    private int id;
    private Double latitude;
    private Double longitude;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Location [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", address=" + address
                + "]";
    }
}
