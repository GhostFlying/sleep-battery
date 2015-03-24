package com.ghostflying.autobatterysaver.fragment;

import com.ghostflying.autobatterysaver.model.Time;
import com.ghostflying.autobatterysaver.util.SettingUtil;

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
