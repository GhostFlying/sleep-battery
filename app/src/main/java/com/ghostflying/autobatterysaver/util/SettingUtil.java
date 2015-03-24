package com.ghostflying.autobatterysaver.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ghost on 3/24/2015.
 */
public class SettingUtil {
    private static final String PREFERENCES_NAME = "Setting";
    private static final String SETTING_ENABLE_NAME = "Enable";
    private static final String SETTING_SNOOZE_NAME = "SnoozeIfActive";

    private static final boolean DEFAULT_ENABLE = false;
    private static final boolean DEFAULT_SNOOZE_IF_ACTIVE = true;

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

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context){
        return getPreferences(context).edit();
    }
}
