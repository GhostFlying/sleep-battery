package com.ghostflying.sleepbattery.util;

/**
 * Created by ghostflying on 3/22/15.
 */
public class BatterySaverModeUtil extends BaseCommandUtil {
    private static String COMMAND_ENABLE = "settings put global low_power 1\n" +
            "am broadcast -a android.os.action.POWER_SAVE_MODE_CHANGED --ez mode true\n";
    private static String COMMAND_DISABLE = "settings put global low_power 0\n" +
            "am broadcast -a android.os.action.POWER_SAVE_MODE_CHANGED --ez mode false\n";

    public static void enable(){
        runCommandWithRoot(COMMAND_ENABLE);
    }

    public static void disable(){
        runCommandWithRoot(COMMAND_DISABLE);
    }
}
