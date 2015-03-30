package com.ghostflying.autobatterysaver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ghostflying.autobatterysaver.BuildConfig;
import com.ghostflying.autobatterysaver.util.AlarmUtil;
import com.ghostflying.autobatterysaver.util.SettingUtil;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompletedReceiver";

    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SettingUtil.isEnable(context)){
            if (BuildConfig.DEBUG){
                Log.d(TAG, "set all alarm.");
            }
            AlarmUtil.setSleepModeAlarm(context);
        }
    }
}
