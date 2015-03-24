package com.ghostflying.autobatterysaver.fragment;

import com.ghostflying.autobatterysaver.model.Time;
import com.ghostflying.autobatterysaver.util.SettingUtil;

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
