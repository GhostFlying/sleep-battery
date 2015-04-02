package com.ghostflying.sleepbattery.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.ghostflying.sleepbattery.BuildConfig;
import com.ghostflying.sleepbattery.service.UserDetectorService;
import com.ghostflying.sleepbattery.service.WorkService;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final String ACTION_START = BuildConfig.APPLICATION_ID + ".START_SLEEP";
    public static final String ACTION_STOP = BuildConfig.APPLICATION_ID + ".STOP_SLEEP";
    public static final String ACTION_START_DELAY = BuildConfig.APPLICATION_ID + ".START_SLEEP_DELAY";
    public static final String ACTION_START_DETECTOR = BuildConfig.APPLICATION_ID + ".START_DETECTOR";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null){
            if (ACTION_START_DETECTOR.equals(intent.getAction())){
                Intent serviceIntent = new Intent(context, UserDetectorService.class);
                startWakefulService(context, serviceIntent);
            }
            else {
                Intent serviceIntent = new Intent(context, WorkService.class);
                serviceIntent.setAction(intent.getAction());
                startWakefulService(context, serviceIntent);
            }
        }
    }
}
