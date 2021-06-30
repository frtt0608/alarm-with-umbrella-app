package com.heon9u.alarm_weather_app.dto;

import java.io.Serializable;

public class Ringtone implements Serializable {
    private String title;
    private String Uri;

    public Ringtone(String title, String Uri) {
        this.title = title;
        this.Uri = Uri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUri(String Uri) {
        this.Uri = Uri;
    }

    public String getUri() {
        return Uri;
    }
}
