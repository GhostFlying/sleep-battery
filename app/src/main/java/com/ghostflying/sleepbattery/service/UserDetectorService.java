package com.ghostflying.sleepbattery.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.ghostflying.sleepbattery.BuildConfig;
import com.ghostflying.sleepbattery.R;
import com.ghostflying.sleepbattery.receiver.AlarmReceiver;
import com.ghostflying.sleepbattery.util.SettingUtil;

import java.util.Date;

public class UserDetectorService extends Service {
    private static final int ONGOING_NOTIFICATION_ID = 1;
    private static final String TAG = "UserDetectorService";

    private boolean isAvailable = true;
    public static Date lastScreenOffTime;

    public UserDetectorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Do not bind this service");
    }

    @Override
    public void onCreate(){
        if (BuildConfig.DEBUG){
            Log.d(TAG, "User detector is on");
        }

        if (SettingUtil.isTodayAvailable(this)){
            lastScreenOffTime = new Date();
            registerScreenOffReceiver();
            setForeground();
        }
        else {
            if (BuildConfig.DEBUG){
                Log.d(TAG, "Today is not available, just skip.");
            }
            isAvailable = false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmReceiver.completeWakefulIntent(intent);
        if (!isAvailable){
            stopSelf();
        }
        return START_STICKY;
    }

    private void setForeground() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_content))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MIN)
                .setOngoing(true);

        startForeground(ONGOING_NOTIFICATION_ID, builder.build());
    }

    private void registerScreenOffReceiver(){
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenOffReceiver, filter);
    }

    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BuildConfig.DEBUG){
                Log.d(TAG, "screen off.");
            }
            lastScreenOffTime = new Date();
        }
    };

    @Override
    public void onDestroy(){
        if (BuildConfig.DEBUG){
            Log.d(TAG, "User detector is off");
        }
        if (isAvailable){
            lastScreenOffTime = null;
            stopForeground(true);
            unregisterReceiver(mScreenOffReceiver);
        }
    }
}
