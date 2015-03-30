package com.ghostflying.autobatterysaver.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import com.ghostflying.autobatterysaver.BuildConfig;
import com.ghostflying.autobatterysaver.receiver.AlarmReceiver;
import com.ghostflying.autobatterysaver.util.AirplaneModeUtil;
import com.ghostflying.autobatterysaver.util.AlarmUtil;
import com.ghostflying.autobatterysaver.util.BatterySaverModeUtil;
import com.ghostflying.autobatterysaver.util.SettingUtil;
import com.ghostflying.autobatterysaver.util.WorkingMode;

import java.util.Date;

public class WorkService extends IntentService {
    private static final long USER_INACTIVITY_THRESHOLD = 1L * 60L * 1000L;
    private static final long DELAYED_TIME_MILLISECONDS = 1L * 60L * 1000L;

    private static final String TAG = "WorkService";

    public WorkService() {
        super("WorkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (BuildConfig.DEBUG){
            Log.d(TAG, "Received intent: " + intent.toString());
        }
        if (intent != null) {
            if (AlarmReceiver.ACTION_START.equals(intent.getAction())
                    || AlarmReceiver.ACTION_START_DELAY.equals(intent.getAction())) {
                enableSleepModeIfNeeded();
            }
            else if (AlarmReceiver.ACTION_STOP.equals(intent.getAction())){
                disableSleepModeIfNeeded();
            }
        }
        AlarmReceiver.completeWakefulIntent(intent);
    }

    private void disableSleepModeIfNeeded(){
        if (BuildConfig.DEBUG){
            Log.d(TAG, "disable sleep mode");
        }
        cancelAllDelayedAlarm();
        setAirplaneMode(false);
        setBatterySaverMode(false);
    }

    private void enableSleepModeIfNeeded() {
        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        Date lastScreenOffTime = UserDetectorService.lastScreenOffTime;
        if (manager.isInteractive()
                || lastScreenOffTime == null // iff the alarm set during the sleep time
                || (new Date().getTime() - lastScreenOffTime.getTime()) < USER_INACTIVITY_THRESHOLD) {
            // screen is on or the user use the device before little time..
            if (BuildConfig.DEBUG){
                Log.d(TAG, "user is active");
            }
            setDelayedAlarm();
            return;
        }

        if (BuildConfig.DEBUG){
            Log.d(TAG, "Sleep mode is on");
        }
        WorkingMode mode = SettingUtil.getWorkingMode(this);
        switch (mode) {
            case BATTERY_SAVER:
                setBatterySaverMode(true);
                break;
            case AIRPLANE:
                setAirplaneMode(true);
                break;
            case BOTH:
                setBatterySaverMode(true);
                setAirplaneMode(true);
        }
        stopService(new Intent(this, UserDetectorService.class));
    }

    private void setDelayedAlarm() {
        long runTime = new Date().getTime() + DELAYED_TIME_MILLISECONDS;
        AlarmUtil.setDelayedAlarm(this, runTime);
    }

    private void cancelAllDelayedAlarm(){
        AlarmUtil.cancelAllDelayedAlarm(this);
    }

    private void setBatterySaverMode(boolean mode) {
        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean batterySaverModeNow = manager.isPowerSaveMode();
        if (batterySaverModeNow != mode) {
            if (mode)
                BatterySaverModeUtil.enable();
            else
                BatterySaverModeUtil.disable();
        }
    }

    private void setAirplaneMode(boolean mode) {
        if (isAirplaneMode() != mode) {
            if (mode)
                AirplaneModeUtil.enable();
            else
                AirplaneModeUtil.disable();
        }
    }

    private boolean isAirplaneMode() {
        return Settings.Global.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }
}
