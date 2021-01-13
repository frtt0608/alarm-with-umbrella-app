package com.heon9u.alarm_weather_app.Activity;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.heon9u.alarm_weather_app.Dto.Alarm;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;
    Alarm alarm;
    Calendar calendar;

//    Intent ringtoneIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        alarm = (Alarm) intent.getExtras().getSerializable("alarm");
        String[] day = alarm.getDay().split(",");
        calendar = Calendar.getInstance();

        if(day.length == 0) {
            // alarm on
            // and alarm off (not repeat)
            AppDatabaseHelper appDB = new AppDatabaseHelper(context);
            appDB.changeTotalFlag(alarm.getId(), false);

        } else {
            AlarmActivity alarmActivity = new AlarmActivity(context, alarm, "update");

            if(alarm.isAllDayFlag()) {
                // alarm on
                // and alarm reSetting(24h)

            } else {
                // check between DAY_OF_WEEK and day
                // if true -> alarm on
                // and alarm reSetting(24h)

                // if false -> alarm reSetting(24h)
            }
        }

//        ringtoneIntent = new Intent(context, RingtoneService.class);
    }
}
