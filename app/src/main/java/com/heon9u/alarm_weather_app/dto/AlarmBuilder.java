package com.heon9u.alarm_weather_app.dto;

public class AlarmBuilder {
    Alarm alarm;

    public AlarmBuilder() {
        alarm = new Alarm();
    }

    public AlarmBuilder setId(int id) {
        alarm.setId(id);
        return this;
    }

    public AlarmBuilder setHour(int hour) {
        alarm.setHour(hour);
        return this;
    }


    public AlarmBuilder setMinute(int minute) {
        alarm.setMinute(minute);
        return this;
    }


    public AlarmBuilder setTitle(String title) {
        alarm.setTitle(title);
        return this;
    }

    public AlarmBuilder setTotalFlag(boolean totalFlag) {
        alarm.setTotalFlag(totalFlag);
        return this;
    }


    public AlarmBuilder setAllDayFlag(boolean allDayFlag) {
        alarm.setAllDayFlag(allDayFlag);
        return this;
    }


    public AlarmBuilder setDay(String day) {
        alarm.setDay(day);
        return this;
    }

    public AlarmBuilder setVolume(int volume) {
        alarm.setVolume(volume);
        return this;
    }

    public AlarmBuilder setBasicSoundFlag(boolean basicSoundFlag) {
        alarm.setBasicSoundFlag(basicSoundFlag);
        return this;
    }

    public AlarmBuilder setBasicSoundTitle(String basicSoundTitle) {
        alarm.setBasicSoundTitle(basicSoundTitle);
        return this;
    }

    public AlarmBuilder setBasicSoundUri(String basicSoundUri) {
        alarm.setBasicSoundUri(basicSoundUri);
        return this;
    }

    public AlarmBuilder setUmbSoundFlag(boolean umbSoundFlag) {
        alarm.setUmbSoundFlag(umbSoundFlag);
        return this;
    }

    public AlarmBuilder setUmbSoundTitle(String umbSoundTitle) {
        alarm.setUmbSoundTitle(umbSoundTitle);
        return this;
    }

    public AlarmBuilder setUmbSoundUri(String umbSoundUri) {
        alarm.setUmbSoundUri(umbSoundUri);
        return this;
    }

    public AlarmBuilder setVibFlag(boolean vibFlag) {
        alarm.setVibFlag(vibFlag);
        return this;
    }

    public AlarmBuilder setLocation_id(int location_id) {
        alarm.setLocation_id(location_id);
        return this;
    }

    public Alarm build() {
        return this.alarm;
    }
}
