package com.heon9u.alarm_weather_app.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(intent.ACTION_BOOT_COMPLETED)) {
            Log.d("BootReceiver", "<<<<<<<<<<<<<<<<< reset >>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }
}
