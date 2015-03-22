package com.ghostflying.autobatterysaver;

import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ghostflying.autobatterysaver.util.BatterySaverModeUtil;

import java.io.DataOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PowerManager manager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        if (manager.isPowerSaveMode()){
            Toast.makeText(this, "Disable battery saver mode.", Toast.LENGTH_SHORT).show();
            BatterySaverModeUtil.disable();
        }
        else {
            Toast.makeText(this, "Enable battery saver mode.", Toast.LENGTH_SHORT).show();
            BatterySaverModeUtil.enable();
        }
    }
}
