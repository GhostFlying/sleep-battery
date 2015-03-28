package com.ghostflying.autobatterysaver.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.ghostflying.autobatterysaver.R;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class WorkingService extends Service {
    private static final int ONGOING_NOTIFICATION_ID = 1;

    private Date lastScreenOffTime;

    public WorkingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Do not bind this service");
    }

    @Override
    public void onCreate(){
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
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lastScreenOffTime = new Date();
            }
        }, filter);
    }

    @Override
    public void onDestroy(){
        stopForeground(true);
    }
}
