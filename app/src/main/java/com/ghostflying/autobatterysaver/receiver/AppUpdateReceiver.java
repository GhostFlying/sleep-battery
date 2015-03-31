package com.ghostflying.autobatterysaver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ghostflying.autobatterysaver.BuildConfig;
import com.ghostflying.autobatterysaver.util.AlarmUtil;
import com.ghostflying.autobatterysaver.util.SettingUtil;

/**
 * Created by ghostflying on 3/31/15.
 */
public class AppUpdateReceiver extends BroadcastReceiver{
    private static final String TAG = "AppUpdateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG){
            Log.d(TAG, "App updated.");
        }
        // set the alarm after app is updated
        if (SettingUtil.isEnable(context)){
            AlarmUtil.setSleepModeAlarm(context);
        }
    }
}
