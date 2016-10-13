package com.android.slw.job;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.slw.R;

/**
 * Created by liwu.shu on 2016/8/17.
 */
public class TestJobSchedule extends Activity implements View.OnClickListener {

    private final static int JOBID = 1;
    Button button;
    JobScheduler jobScheduler;
    JobInfo.Builder builder;
    int jobId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jobschedule);
        initValues();
        initViews();
        bindListener();
    }

    private void initViews(){
        button = (Button)findViewById(R.id.start);
    }

    private void bindListener(){
        button.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initValues(){
        jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.start:
                invokeJobSchedule();
            break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void invokeJobSchedule(){
        if(jobId>0){
            jobScheduler.cancel(jobId);
            jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        ComponentName componentName = new ComponentName(getPackageName(),TestJobScheduleService.class.getName());
        builder = new JobInfo.Builder(JOBID,componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
//        builder.setPeriodic(5000);
//        builder.setOverrideDeadline(5000);
        jobId =jobScheduler.schedule(builder.build());
        System.out.println("invokeJobSchedule: "+jobId);
    }
}
