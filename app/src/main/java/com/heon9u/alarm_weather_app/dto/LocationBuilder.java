package com.heon9u.alarm_weather_app.dto;

public class LocationBuilder {
    Location location;

    public LocationBuilder() {
        location = new Location();
    }

    public LocationBuilder setId(int id) {
        location.setId(id);
        return this;
    }

    public LocationBuilder setOrderNum(int orderNum) {
        location.setOrderNum(orderNum);
        return this;
    }

    public LocationBuilder setStreetAddress(String streetAddress) {
        location.setStreetAddress(streetAddress);
        return this;
    }

    public LocationBuilder setLotAddress(String lotAddress) {
        location.setLotAddress(lotAddress);
        return this;
    }

    public LocationBuilder setCommunityCenter(String communityCenter) {
        location.setCommunityCenter(communityCenter);
        return this;
    }

    public LocationBuilder setLatitude(Double latitude) {
        location.setLatitude(latitude);
        return this;
    }

    public LocationBuilder setLongitude(Double longitude) {
        location.setLongitude(longitude);
        return this;
    }

    public LocationBuilder setTmX(Double tmX) {
        location.setTmX(tmX);
        return this;
    }

    public LocationBuilder setTmY(Double tmY) {
        location.setTmY(tmY);
        return this;
    }

    public Location build() {
        return this.location;
    }
}