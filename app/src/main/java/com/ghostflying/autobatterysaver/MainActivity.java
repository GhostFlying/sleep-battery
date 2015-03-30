package com.ghostflying.autobatterysaver;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ghostflying.autobatterysaver.fragment.BaseAlertDialogFragment;
import com.ghostflying.autobatterysaver.fragment.BaseTimePickerDialog;
import com.ghostflying.autobatterysaver.fragment.ChooseDialogFragment;
import com.ghostflying.autobatterysaver.fragment.EndTimePicker;
import com.ghostflying.autobatterysaver.fragment.StartTimePicker;
import com.ghostflying.autobatterysaver.model.Time;
import com.ghostflying.autobatterysaver.util.AlarmUtil;
import com.ghostflying.autobatterysaver.util.SettingUtil;
import com.ghostflying.autobatterysaver.util.WorkingMode;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity
        implements BaseTimePickerDialog.TimePickerDialogInteraction,
        BaseAlertDialogFragment.OnFragmentInteractionListener {
    private static final String TIME_TEMPLATE = "%02d:%02d";

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.disable_overlay)
    View mDisableOverlay;
    @InjectView(R.id.snooze_if_active_checkbox)
    CheckBox mSnoozeIfActiveCheckBox;
    @InjectView(R.id.start_time_text)
    TextView mStartTimeText;
    @InjectView(R.id.end_time_text)
    TextView mEndTimeText;
    @InjectView(R.id.working_mode_text)
    TextView mWorkingModeText;
    @InjectView(R.id.available_days_text)
    TextView mAvailableDaysText;

    SwitchCompat mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialView();
    }

    private void initialView() {
        ButterKnife.inject(this);
        setToolbar();
        mSnoozeIfActiveCheckBox.setChecked(SettingUtil.isSnoozeIfActive(this));
        setTime();
        setWorkingMode();
        setAvailableDays();
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setTime() {
        setStartTime();
        setEndTime();
    }
    private void setStartTime() {
        Time startTime = SettingUtil.getStartTime(this);
        mStartTimeText.setText(
                String.format(TIME_TEMPLATE, startTime.getHour(), startTime.getMinute()));
    }

    private void setEndTime() {
        Time endTime = SettingUtil.getEndTime(this);
        mEndTimeText.setText(
                String.format(TIME_TEMPLATE, endTime.getHour(), endTime.getMinute())
        );
    }

    private void setWorkingMode() {
        mWorkingModeText.setText(SettingUtil.getWorkingMode(this).getStringRes());
    }

    private void setAvailableDays() {
        String resultText = null;
        boolean[] availableArray = getAvailableDaysArray();
        String[] shortDaysOfWeek = getResources().getStringArray(R.array.short_days_array);
        for (int i = 0; i < availableArray.length; i++) {
            if (availableArray[i]) {
                if (resultText == null) {
                    resultText = shortDaysOfWeek[i];
                } else {
                    resultText += ", " + shortDaysOfWeek[i];
                }
            }
        }
        if (resultText == null)
            resultText = getResources().getString(R.string.day_none);
        mAvailableDaysText.setText(resultText);
    }

    private boolean[] getAvailableDaysArray() {
        Map<String, Boolean> availableDays = SettingUtil.getAvailableDays(this);
        String[] daysOfWeek = getResources().getStringArray(R.array.days_array);
        boolean[] result = new boolean[daysOfWeek.length];
        for (int i = 0; i < daysOfWeek.length; i++) {
            if (availableDays.get(daysOfWeek[i]))
                result[i] = true;
            else
                result[i] = false;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSwitchItem = menu.findItem(R.id.action_switch);
        mSwitch = (SwitchCompat) mSwitchItem.getActionView().findViewById(R.id.toolbar_switch);
        mSwitch.setChecked(SettingUtil.isEnable(this));
        setOverlay(mSwitch.isChecked());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingUtil.setEnable(MainActivity.this, isChecked);
                setOverlay(isChecked);
                setAlarm(isChecked);
            }
        });
        return true;
    }

    private void setAlarm(boolean isEnable){
        if (isEnable){
            AlarmUtil.setSleepModeAlarm(this);
        }
        else {
            AlarmUtil.cancelAllAlarm(this);
        }
    }

    private void setOverlay(boolean isEnable) {
        if (isEnable) {
            mDisableOverlay.setVisibility(View.GONE);
        } else {
            mDisableOverlay.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.start_time, R.id.end_time, R.id.mode_switch, R.id.available_days, R.id.snooze_if_active})
    void onSettingItemClicked(View view) {
        switch (view.getId()) {
            case R.id.snooze_if_active:
                mSnoozeIfActiveCheckBox.setChecked(!mSnoozeIfActiveCheckBox.isChecked());
                break;
            case R.id.start_time:
                DialogFragment startTimeDialog = new StartTimePicker();
                startTimeDialog.show(getFragmentManager(), null);
                break;
            case R.id.end_time:
                DialogFragment endTimeDialog = new EndTimePicker();
                endTimeDialog.show(getFragmentManager(), null);
                break;
            case R.id.mode_switch:
                DialogFragment dialogFragment = ChooseDialogFragment
                        .newInstance(
                                R.string.mode_switch_dialog_title,
                                R.array.mode_array,
                                SettingUtil.getWorkingMode(this).ordinal()
                        );
                dialogFragment.show(getFragmentManager(), null);
                break;
            case R.id.available_days:
                DialogFragment dialogFragment1 = ChooseDialogFragment
                        .newInstance(
                                R.string.days_choose_dialog_title,
                                R.array.days_array,
                                getAvailableDaysArray()
                        );
                dialogFragment1.show(getFragmentManager(), null);
                break;
        }
    }

    @OnCheckedChanged(R.id.snooze_if_active_checkbox)
    void onCheckBoxCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SettingUtil.setSnoozeIfActive(this, isChecked);
    }

    @Override
    public void onTimeSet(Time time) {
        setTime();
        AlarmUtil.setSleepModeAlarm(this);
    }

    @Override
    public void onPositiveButtonClick(Bundle value, int title) {
        if (title == R.string.mode_switch_dialog_title) {
            SettingUtil.setWorkingMode(this, WorkingMode.values()[value.getInt(ChooseDialogFragment.ARG_ITEM_CHECKED)]);
            setWorkingMode();
        } else if (title == R.string.days_choose_dialog_title) {
            SettingUtil.setAvailableDays(this, value.getBooleanArray(ChooseDialogFragment.ARG_ITEM_CHECKED));
            setAvailableDays();
        }
    }

    @Override
    public void onNegativeButtonClick(Bundle value, int title) {

    }
}
