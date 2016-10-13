package com.android.slw.activity;


import com.android.slw.R;
import com.tcl.launcher.effect.page.IEffectFactory;
import com.tcl.launcher.effect.Workspace;
import com.tcl.launcher.effect.ball.BallEffectorFactory;

import java.io.IOException;

/**
 * Created by liwu.shu on 2016/8/27.
 */
public class TestEffectActivity extends BaseActivity {
    Workspace workspace;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_effect_layout);
    }

    @Override
    protected void initViews()  {
        workspace = (Workspace)findViewById(R.id.workspace);
        IEffectFactory factory = new BallEffectorFactory();
        workspace.setEffector(factory.createEffector());
        workspace.setPageLoop(true);//设置页面循环
        throw new RuntimeException("");
    }

    @Override
    protected void bindListener() {

    }
}
