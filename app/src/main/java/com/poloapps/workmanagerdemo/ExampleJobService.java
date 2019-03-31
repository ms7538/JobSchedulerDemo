package com.poloapps.workmanagerdemo;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.util.Log;

public class ExampleJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    private static final int JS_ID = 943292346;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG,"Job started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params ){
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 10; i++){
                    Log.d(TAG, "run: " + i);
                    if(i == 5){cancelJob();}
                    if (jobCancelled){ return; }
                    try {Thread.sleep(500);}
                    catch (InterruptedException e) {e.printStackTrace(); }
                }
                Log.d(TAG, "Job Finished");
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
    public void cancelJob(){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        assert scheduler != null;
        scheduler.cancel(JS_ID);
        Log.d(TAG, "Job stopped from within");
    }
}
