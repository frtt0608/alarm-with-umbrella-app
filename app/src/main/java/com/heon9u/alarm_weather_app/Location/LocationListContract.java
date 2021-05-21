package com.heon9u.alarm_weather_app.Location;

import com.heon9u.alarm_weather_app.Dto.Location;

import java.util.ArrayList;

public interface LocationListContract {

    interface View {
        void hideLocationList();
        void showLocationList();
        void setItems(ArrayList<Location> items);

    }

    interface Presenter<View> {
        void setView();
        void releaseView();
        void loadLocationList();
    }
}
