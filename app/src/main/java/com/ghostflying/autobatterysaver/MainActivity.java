package com.ghostflying.autobatterysaver;

import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ghostflying.autobatterysaver.util.BatterySaverModeUtil;
import com.ghostflying.autobatterysaver.util.SettingUtil;

import java.io.DataOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.disable_overlay)
    View mDisableOverlay;

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
}
