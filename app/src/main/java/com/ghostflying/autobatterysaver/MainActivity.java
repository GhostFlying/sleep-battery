package com.ghostflying.autobatterysaver;

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

import com.ghostflying.autobatterysaver.util.SettingUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.disable_overlay)
    View mDisableOverlay;
    @InjectView(R.id.snooze_if_active_checkbox)
    CheckBox mSnoozeIfActiveCheckBox;

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
    }

    private void setToolbar(){
        setSupportActionBar(mToolbar);
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
        }
    }

    @OnCheckedChanged(R.id.snooze_if_active_checkbox)
    void onCheckBoxCheckedChanged(CompoundButton buttonView, boolean isChecked){
        SettingUtil.setSnoozeIfActive(this, isChecked);
    }
}
