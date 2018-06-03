package com.kj.kevin.hitsmusic;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.util.Log;

/**
 * Created by Kevin on 2018/6/3.
 */

public class JobSchedulerService extends JobService {
    public static final String TAG = "JobSchedule";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob: ");

        // check now activity is loading data successfully or get data

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "onStopJob: ");

        return false;
    }
}
