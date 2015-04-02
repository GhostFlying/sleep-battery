package com.ghostflying.sleepbattery.fragment;

import com.ghostflying.sleepbattery.model.Time;
import com.ghostflying.sleepbattery.util.SettingUtil;

/**
 * Created by ghostflying on 3/24/15.
 */
public class StartTimePicker extends BaseTimePickerDialog {
    @Override
    Time getTimeFromSetting() {
        return SettingUtil.getStartTime(getActivity());
    }

    @Override
    void setTimeToSetting(Time time) {
        SettingUtil.setStartTime(getActivity(), time);
    }
}
