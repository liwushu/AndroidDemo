package com.android.slw.job;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

/**
 * Created by liwu.shu on 2016/8/17.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TestJobScheduleService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        System.out.println("TestJobScheduleService onStartJob");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        System.out.println("onStopJob");
        return false;
    }
}
