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
import com.ghostflying.autobatterysaver.fragment.EndTimePicker;
import com.ghostflying.autobatterysaver.fragment.SingleChooseDialogFragment;
import com.ghostflying.autobatterysaver.fragment.StartTimePicker;
import com.ghostflying.autobatterysaver.model.Time;
import com.ghostflying.autobatterysaver.util.SettingUtil;
import com.ghostflying.autobatterysaver.util.WorkingMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity
        implements BaseTimePickerDialog.TimePickerDialogInteraction,
        BaseAlertDialogFragment.OnFragmentInteractionListener{
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

    SwitchCompat mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialView();
    }

    private void initialView(){
        ButterKnife.inject(this);
        setToolbar();
        mSnoozeIfActiveCheckBox.setChecked(SettingUtil.isSnoozeIfActive(this));
        setTime();
        setWorkingMode();
    }

    private void setToolbar(){
        setSupportActionBar(mToolbar);
    }

    private void setTime(){
        setStartTime();
        setEndTime();
    }

    private void setStartTime(){
        Time startTime = SettingUtil.getStartTime(this);
        mStartTimeText.setText(
                String.format(TIME_TEMPLATE, startTime.getHour(), startTime.getMinute()));
    }

    private void setEndTime(){
        Time endTime = SettingUtil.getEndTime(this);
        mEndTimeText.setText(
                String.format(TIME_TEMPLATE, endTime.getHour(), endTime.getMinute())
        );
    }

    private void setWorkingMode(){
        mWorkingModeText.setText(SettingUtil.getWorkingMode(this).getStringRes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSwitchItem = menu.findItem(R.id.action_switch);
        mSwitch = (SwitchCompat)mSwitchItem.getActionView().findViewById(R.id.toolbar_switch);
        mSwitch.setChecked(SettingUtil.isEnable(this));
        setOverlay(mSwitch.isChecked());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingUtil.setEnable(MainActivity.this, isChecked);
                setOverlay(isChecked);
            }
        });
        return true;
    }

    private void setOverlay(boolean isEnable){
        if (isEnable){
            mDisableOverlay.setVisibility(View.GONE);
        }
        else {
            mDisableOverlay.setVisibility(View.VISIBLE);
        }
    }

    @OnClick ({R.id.start_time, R.id.end_time, R.id.mode_switch, R.id.available_days, R.id.snooze_if_active})
    void onSettingItemClicked(View view){
        switch (view.getId()){
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
                DialogFragment dialogFragment = SingleChooseDialogFragment
                        .newInstance(
                                R.string.mode_switch_dialog_title,
                                R.array.mode_array,
                                SettingUtil.getWorkingMode(this).ordinal()
                        );
                dialogFragment.show(getFragmentManager(), null);
                break;

        }
    }

    @OnCheckedChanged(R.id.snooze_if_active_checkbox)
    void onCheckBoxCheckedChanged(CompoundButton buttonView, boolean isChecked){
        SettingUtil.setSnoozeIfActive(this, isChecked);
    }

    @Override
    public void onTimeSet(Time time) {
        setTime();
    }

    @Override
    public void onPositiveButtonClick(int value, int title) {
        if (title == R.string.mode_switch_dialog_title){
            SettingUtil.setWorkingMode(this, WorkingMode.values()[value]);
            setWorkingMode();
        }
    }

    @Override
    public void onNegativeButtonClick(int value, int title) {

    }
}
