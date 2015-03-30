package com.ghostflying.autobatterysaver.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ghostflying.autobatterysaver.model.Time;
import com.ghostflying.autobatterysaver.receiver.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by Ghost on 3/29/2015.
 */
public class AlarmUtil {
    private static final int START_ACTION_PENDING_REQUEST_ID = 10;
    private static final int STOP_ACTION_PENDING_REQUEST_ID = 20;
    private static final int USER_DETECTOR_PENDING_REQUEST_ID = 30;
    private static final long USER_DETECTOR_BEFORE_TIME_IN_MILLISECONDS = 15L * 60L * 1000L;

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

    public static void setSleepModeAlarm(Context context){
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

        Intent userDetectorIntent = new Intent(context, AlarmReceiver.class);
        userDetectorIntent.setAction(AlarmReceiver.ACTION_START_DETECTOR);
        PendingIntent userDetectorPending =
                PendingIntent.getBroadcast(
                        context,
                        USER_DETECTOR_PENDING_REQUEST_ID,
                        userDetectorIntent,
                        0
                );

        Intent startIntent = new Intent(context, AlarmReceiver.class);
        startIntent.setAction(AlarmReceiver.ACTION_START);
        PendingIntent startPending =
                PendingIntent.getBroadcast(
                        context,
                        START_ACTION_PENDING_REQUEST_ID,
                        startIntent,
                        0
                );

        Intent endIntent = new Intent(context, AlarmReceiver.class);
        endIntent.setAction(AlarmReceiver.ACTION_STOP);
        PendingIntent endPending =
                PendingIntent.getBroadcast(
                        context,
                        STOP_ACTION_PENDING_REQUEST_ID,
                        endIntent,
                        0
                );

        //cancel exist alarm
        cancelAlarm(context, userDetectorPending);
        cancelAlarm(context, startPending);
        cancelAlarm(context, endPending);

        setIntervalDayAlarm(context, userDetectorPending, userDetectorAlarmTime);
        setIntervalDayAlarm(context, startPending, startAlarmTime);
        setIntervalDayAlarm(context, endPending, endAlarmTime);
    }
}
