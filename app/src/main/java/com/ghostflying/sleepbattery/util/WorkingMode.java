package com.ghostflying.sleepbattery.util;

import com.ghostflying.sleepbattery.R;

/**
 * Created by ghostflying on 3/24/15.
 */
public enum WorkingMode {
    // should be in the same order of the resource
    BATTERY_SAVER, AIRPLANE, BOTH;

    public int getStringRes(){
        switch (this){
            case BATTERY_SAVER:
                return R.string.mode_battery_saver;
            case AIRPLANE:
                return R.string.mode_airplane;
            case BOTH:
                return R.string.mode_both;
            default:
                return R.string.mode_battery_saver;
        }
    }
}
