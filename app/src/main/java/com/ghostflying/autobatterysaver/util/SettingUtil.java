package com.ghostflying.autobatterysaver.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ghostflying.autobatterysaver.R;
import com.ghostflying.autobatterysaver.model.Time;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ghost on 3/24/2015.
 */
public class SettingUtil {
    private static final String PREFERENCES_NAME = "Setting";
    private static final String SETTING_ENABLE_NAME = "Enable";
    private static final String SETTING_SNOOZE_NAME = "SnoozeIfActive";
    private static final String SETTING_START_TIME_HOUR_NAME = "StartTimeHour";
    private static final String SETTING_START_TIME_MINUTE_NAME = "StartTimeMinute";
    private static final String SETTING_END_TIME_HOUR_NAME = "EndTimeHour";
    private static final String SETTING_END_TIME_MINUTE_NAME = "EndTimeMinute";
    private static final String SETTING_WORKING_MODE_NAME = "WorkingMode";
    private static final String SETTING_AVAILABLE_DAYS_SUFFIX = "/Available";

    private static final boolean DEFAULT_ENABLE = false;
    private static final boolean DEFAULT_SNOOZE_IF_ACTIVE = true;
    private static final int DEFAULT_START_TIME_HOUR = 0;
    private static final int DEFAULT_START_TIME_MINUTE = 0;
    private static final int DEFAULT_END_TIME_HOUR = 8;
    private static final int DEFAULT_END_TIME_MINUTE = 0;
    private static final String DEFAULT_WORKING_MODE = WorkingMode.BATTERY_SAVER.name();
    private static final boolean DEFAULT_DAYS_AVAILABLE = true;

    public static boolean isEnable(Context context){
        return getPreferences(context).getBoolean(SETTING_ENABLE_NAME, DEFAULT_ENABLE);
    }

    public static void setEnable(Context context, boolean isEnable){
        getEditor(context).putBoolean(SETTING_ENABLE_NAME, isEnable).apply();
    }

    public static boolean isSnoozeIfActive(Context context){
        return getPreferences(context).getBoolean(SETTING_SNOOZE_NAME, DEFAULT_SNOOZE_IF_ACTIVE);
    }

    public static void setSnoozeIfActive(Context context, boolean isSnooze){
        getEditor(context).putBoolean(SETTING_SNOOZE_NAME, isSnooze).apply();
    }

    public static Time getStartTime(Context context){
        SharedPreferences preferences = getPreferences(context);
        return new Time(
                preferences.getInt(SETTING_START_TIME_HOUR_NAME, DEFAULT_START_TIME_HOUR),
                preferences.getInt(SETTING_START_TIME_MINUTE_NAME, DEFAULT_START_TIME_MINUTE)
        );
    }

    public static void setStartTime(Context context, Time time){
        getEditor(context)
                .putInt(SETTING_START_TIME_HOUR_NAME, time.getHour())
                .putInt(SETTING_START_TIME_MINUTE_NAME, time.getMinute())
                .apply();
    }

    public static Time getEndTime(Context context){
        SharedPreferences preferences = getPreferences(context);
        return new Time(
                preferences.getInt(SETTING_END_TIME_HOUR_NAME, DEFAULT_END_TIME_HOUR),
                preferences.getInt(SETTING_END_TIME_MINUTE_NAME, DEFAULT_END_TIME_MINUTE)
        );
    }

    public static void setEndTime(Context context, Time endTime){
        getEditor(context)
                .putInt(SETTING_END_TIME_HOUR_NAME, endTime.getHour())
                .putInt(SETTING_END_TIME_MINUTE_NAME, endTime.getMinute())
                .apply();
    }

    public static WorkingMode getWorkingMode(Context context){
        return WorkingMode.valueOf(getPreferences(context).getString(SETTING_WORKING_MODE_NAME, DEFAULT_WORKING_MODE));
    }

    public static void setWorkingMode(Context context, WorkingMode mode){
        getEditor(context).putString(SETTING_WORKING_MODE_NAME, mode.name()).apply();
    }

    public static Map<String, Boolean> getAvailableDays(Context context){
        Map<String, Boolean> days = new HashMap<>();
        String[] daysOfWeek = context.getResources().getStringArray(R.array.days_array);
        SharedPreferences preferences = getPreferences(context);
        for (int i = 0; i < daysOfWeek.length; i++){
            //use for to hold the order
            days.put(
                    daysOfWeek[i],
                    preferences.getBoolean(
                            daysOfWeek[i] + SETTING_AVAILABLE_DAYS_SUFFIX,
                            DEFAULT_DAYS_AVAILABLE
                    )
            );
        }
        return days;
    }

    public static void setAvailableDays(Context context, boolean[] available){
        SharedPreferences.Editor editor = getEditor(context);
        String[] daysOfWeek = context.getResources().getStringArray(R.array.days_array);
        for (int i = 0; i < daysOfWeek.length; i++){
            //use for to hold the order
            editor.putBoolean(
                    daysOfWeek[i] + SETTING_AVAILABLE_DAYS_SUFFIX,
                    available[i]
            );
        }
        editor.apply();
    }

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context){
        return getPreferences(context).edit();
    }
}
