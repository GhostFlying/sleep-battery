package com.ghostflying.sleepbattery.fragment;

import com.ghostflying.sleepbattery.model.Time;
import com.ghostflying.sleepbattery.util.SettingUtil;

/**
 * Created by ghostflying on 3/24/15.
 */
public class EndTimePicker extends BaseTimePickerDialog {
    @Override
    Time getTimeFromSetting() {
        return SettingUtil.getEndTime(getActivity());
    }

    @Override
    void setTimeToSetting(Time time) {
        SettingUtil.setEndTime(getActivity(), time);
    }
}
