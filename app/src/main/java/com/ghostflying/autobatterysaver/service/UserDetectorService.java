package com.ghostflying.autobatterysaver.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.ghostflying.autobatterysaver.BuildConfig;
import com.ghostflying.autobatterysaver.R;

import java.util.Date;

public class UserDetectorService extends Service {
    private static final int ONGOING_NOTIFICATION_ID = 1;
    private static final String TAG = "UserDetectorService";

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
        lastScreenOffTime = new Date();
        registerScreenOffReceiver();
        setForeground();
    }

    private void setForeground() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.notification_title))
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
        lastScreenOffTime = null;
        stopForeground(true);
        unregisterReceiver(mScreenOffReceiver);
    }
}
