package com.ghostflying.sleepbattery.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ghostflying.sleepbattery.BuildConfig;
import com.ghostflying.sleepbattery.model.Time;
import com.ghostflying.sleepbattery.receiver.AlarmReceiver;
import com.ghostflying.sleepbattery.service.UserDetectorService;
import com.ghostflying.sleepbattery.service.WorkService;

import java.util.Calendar;

/**
 * Created by Ghost on 3/29/2015.
 */
public class AlarmUtil {
    private static final int START_ACTION_PENDING_REQUEST_ID = 10;
    private static final int STOP_ACTION_PENDING_REQUEST_ID = 20;
    private static final int USER_DETECTOR_PENDING_REQUEST_ID = 30;
    private static final int START_INTENT_REQUEST_CODE = 40;
    private static final long USER_DETECTOR_BEFORE_TIME_IN_MILLISECONDS = 15L * 60L * 1000L;
    private static final String TAG = "AlarmUtil";

    private static void setIntervalDayAlarm(Context context, PendingIntent intent, long time){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY,
                intent
        );
    }

    public static void cancelAlarm(Context context, PendingIntent intent){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);
    }

    public static void setAlarm(Context context, PendingIntent intent, long time){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                time,
                intent
        );
    }

    public synchronized static void setSleepModeAlarm(Context context){
        if (BuildConfig.DEBUG){
            Log.d(TAG, "Start set sleep mode alarm.");
        }

        long startAlarmTime;
        long endAlarmTime;
        long userDetectorAlarmTime;

        Time startTime = SettingUtil.getStartTime(context);
        Time endTime = SettingUtil.getEndTime(context);

        // start to find out the correct alarm time.
        Calendar calendar = Calendar.getInstance();
        long currentTime = System.currentTimeMillis();
        calendar.setTimeInMillis(currentTime);

        // first to determine end time
        // this should be the closet but passed time
        calendar.set(Calendar.HOUR_OF_DAY, endTime.getHour());
        calendar.set(Calendar.MINUTE, endTime.getMinute());
        // if the end time is passed, add one day
        if (calendar.getTimeInMillis() < currentTime){
            calendar.add(Calendar.DATE, 1);
        }
        // determined
        endAlarmTime = calendar.getTimeInMillis();

        // start to determine start alarm time
        calendar.set(Calendar.HOUR_OF_DAY, startTime.getHour());
        calendar.set(Calendar.MINUTE, startTime.getMinute());
        // make sure the start time is before end time
        if (calendar.getTimeInMillis() > endAlarmTime){
            calendar.add(Calendar.DATE, -1);
        }
        startAlarmTime = calendar.getTimeInMillis();

        userDetectorAlarmTime = startAlarmTime - USER_DETECTOR_BEFORE_TIME_IN_MILLISECONDS;

        PendingIntent userDetectorPending = getUserDetectorPendingIntent(context);

        PendingIntent startPending = getStartPendingIntent(context);

        PendingIntent endPending = getEndPendingIntent(context);

        //cancel exist alarm
        cancelAlarm(context, userDetectorPending);
        cancelAlarm(context, startPending);
        cancelAlarm(context, endPending);

        //stop running service
        stopRunningService(context);

        ThreadUtil.delayAlarmEnable = true;
        if (SettingUtil.isSnoozeIfActive(context)){
            setIntervalDayAlarm(context, userDetectorPending, userDetectorAlarmTime);
        }
        setIntervalDayAlarm(context, startPending, startAlarmTime);
        setIntervalDayAlarm(context, endPending, endAlarmTime);
    }

    private static void stopRunningService(Context context) {
        if (BuildConfig.DEBUG){
            Log.d(TAG, "Start stop all service.");
        }

        context.stopService(new Intent(context, UserDetectorService.class));
        context.stopService(new Intent(context, WorkService.class));
    }

    public synchronized static void setDelayedAlarm(Context context, long time){
        if (ThreadUtil.delayAlarmEnable)
            setAlarm(context, getDelayedPendingIntent(context), time);
    }

    public synchronized static void cancelAllDelayedAlarm(Context context){
        cancelAlarm(context, getDelayedPendingIntent(context));
    }

    public synchronized static void cancelAllAlarm(Context context){
        if (BuildConfig.DEBUG){
            Log.d(TAG, "Start cancel all alarm.");
        }

        // the sync and the flag to make sure the service is stop correctly and
        // not delay alarm is set.
        ThreadUtil.delayAlarmEnable = false;
        cancelAlarm(context, getUserDetectorPendingIntent(context));
        cancelAlarm(context, getStartPendingIntent(context));
        cancelAlarm(context, getEndPendingIntent(context));
        cancelAlarm(context, getDelayedPendingIntent(context));
        stopRunningService(context);
    }

    private static PendingIntent getEndPendingIntent(Context context) {
        Intent endIntent = new Intent(context, AlarmReceiver.class);
        endIntent.setAction(AlarmReceiver.ACTION_STOP);
        return PendingIntent.getBroadcast(
                context,
                STOP_ACTION_PENDING_REQUEST_ID,
                endIntent,
                0
        );
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent startIntent = new Intent(context, AlarmReceiver.class);
        startIntent.setAction(AlarmReceiver.ACTION_START);
        return PendingIntent.getBroadcast(
                context,
                START_ACTION_PENDING_REQUEST_ID,
                startIntent,
                0
        );
    }

    private static PendingIntent getUserDetectorPendingIntent(Context context) {
        Intent userDetectorIntent = new Intent(context, AlarmReceiver.class);
        userDetectorIntent.setAction(AlarmReceiver.ACTION_START_DETECTOR);
        return PendingIntent.getBroadcast(
                context,
                USER_DETECTOR_PENDING_REQUEST_ID,
                userDetectorIntent,
                0
        );
    }

    private static PendingIntent getDelayedPendingIntent(Context context) {
        Intent delayedIntent = new Intent(context, AlarmReceiver.class);
        delayedIntent.setAction(AlarmReceiver.ACTION_START_DELAY);
        return PendingIntent.getBroadcast(
                context,
                START_INTENT_REQUEST_CODE,
                delayedIntent,
                0
        );
    }
}
