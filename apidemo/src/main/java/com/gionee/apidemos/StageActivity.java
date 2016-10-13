
package com.gionee.apidemos;

import com.gionee.clip.animation.AnimationDirector;
import com.gionee.clip.utils.ClipLog;
import com.gionee.clip.view.StageGLSurfaceView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.app.Activity;
import android.content.Intent;

public class StageActivity extends Activity {

    public static final String PATHARRAY = "patharray";
    StageGLSurfaceView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.clip_main);

        mView = (StageGLSurfaceView) findViewById(R.id.stage);
        Intent intent = getIntent();
        String[] paths = intent.getStringArrayExtra(PATHARRAY);
        int[] ids;
        if (!ClipLog.DEMO) {
            if (null == paths || 0 == paths.length) {
                finish();
            }
        } else {
            ids = new int[] {
                    R.drawable.clippage1, R.drawable.clippage2,
                    R.drawable.clippage3, R.drawable.clippage4
            };
        }
        mView.init(paths, ids);

        Switch vSwitch = (Switch) findViewById(R.id.switch_loopmode);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_type);
        if (vSwitch.isChecked()) {
            spinner.setEnabled(false);
        } else {
            spinner.setEnabled(true);
        }
        vSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    mView.setSlideMode(AnimationDirector.SLIDE_AUTO);
                    spinner.setEnabled(false);
                } else {
                    mView.setSlideMode(AnimationDirector.SLIDE_GESTURE);
                    spinner.setEnabled(true);
                    spinner.setSelection(0);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.animation_type, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mView.setAniType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mView.setAniType(0);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
