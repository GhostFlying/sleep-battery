package com.ghostflying.autobatterysaver.util;

/**
 * Created by Ghost on 3/23/2015.
 */
public class AirplaneModeUtil extends BaseCommandUtil{
    private static String COMMAND_ENABLE = "settings put global airplane_mode_on 1\n" +
            "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n";
    private static String COMMAND_DISABLE = "settings put global airplane_mode_on 0\n" +
            "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n";

    public static void enable(){
        runCommandWithRoot(COMMAND_ENABLE);
    }

    public static void disable(){
        runCommandWithRoot(COMMAND_DISABLE);
    }
}
