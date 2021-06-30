package com.heon9u.alarm_weather_app.anotherTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(intent.ACTION_BOOT_COMPLETED)) {
            Intent bootIntent = new Intent(context, BootService.class);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(bootIntent);
            } else {
                context.startService(bootIntent);
            }
        }
    }
}
