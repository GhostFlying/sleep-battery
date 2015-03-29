package com.ghostflying.autobatterysaver.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ghostflying.autobatterysaver.model.Time;
import com.ghostflying.autobatterysaver.service.UserDetectorService;
import com.ghostflying.autobatterysaver.service.WorkService;

import java.util.Calendar;

/**
 * Created by Ghost on 3/29/2015.
 */
public class AlarmUtil {
    private static final int START_ACTION_PENDING_REQUEST_ID = 10;
    private static final int STOP_ACTION_PENDING_REQUEST_ID = 20;
    private static final int USER_DETECTOR_PENDING_REQUEST_ID = 30;

    private static boolean setIntervalDayAlarm(Context context, PendingIntent intent, Time time, boolean delayToNextDay){
        boolean delay = false;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinute());

        if (delayToNextDay || calendar.getTimeInMillis() < System.currentTimeMillis()){
            // the time is passed
            calendar.add(Calendar.DATE, 1);
            delay = true;
        }

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                intent
        );
        return delay;
    }

    public static void cancelAlarm(Context context, PendingIntent intent){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);
    }

    public static void setStartAlarm(Context context){
        Intent startIntent = new Intent(context, WorkService.class);
        startIntent.setAction(WorkService.ACTION_START);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                START_ACTION_PENDING_REQUEST_ID,
                startIntent,
                0
        );
        // cancel previous alarm
        cancelAlarm(context, pendingIntent);
        Time startTime = SettingUtil.getStartTime(context);
        boolean isDelayed = setUserDetectorAlarm(context, startTime);
        setIntervalDayAlarm(context, pendingIntent, startTime, isDelayed);
    }

    public static void setEndAlarm(Context context){
        Intent endIntent = new Intent(context, WorkService.class);
        endIntent.setAction(WorkService.ACTION_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                STOP_ACTION_PENDING_REQUEST_ID,
                endIntent,
                0
        );
        // cancel previous alarm
        cancelAlarm(context, pendingIntent);
        setIntervalDayAlarm(context, pendingIntent, SettingUtil.getEndTime(context), false);
    }

    private static boolean setUserDetectorAlarm(Context context, Time startTime){
        Time startDetectorTime;
        if (startTime.getMinute() > 30)
            startDetectorTime = new Time(startTime.getHour(), startTime.getMinute());
        else {
            if (startTime.getHour() > 1)
                startDetectorTime = new Time(startTime.getHour() - 1, startTime.getMinute());
            else
                startDetectorTime = new Time(23, startTime.getMinute());
        }

        Intent detectorIntent = new Intent(context, UserDetectorService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                USER_DETECTOR_PENDING_REQUEST_ID,
                detectorIntent,
                0
        );
        cancelAlarm(context, pendingIntent);
        return setIntervalDayAlarm(context, pendingIntent, startDetectorTime, false);
    }

    public static void setAlarm(Context context, PendingIntent intent, long time){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                time,
                intent
        );
    }
}
