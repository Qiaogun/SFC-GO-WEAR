package com.sfc.myapplication;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
//废弃
public class JobSchedulerService extends JobService {
    private static final String TAG = "JobSchedulerService";
    private static final int JOB_ID = 1;
    private JobScheduler jobScheduler;

    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleJob();
        Log.d(TAG, "onStartCommand: ");
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        Intent service = new Intent(getApplicationContext(), WifiScanService.class);
        getApplicationContext().startService(service);
        // Do your work here
        // ...
        return true; // true if there is more work to be done, false if it's finished
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        // return true if you want to reschedule the job
        return true;
    }
    public void scheduleJob() {
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(getPackageName(),
                JobSchedulerService.class.getName()));
        builder.setPeriodic(3000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);

        if (jobScheduler.schedule(builder.build()) <= 0) {
            Log.d("JobSchedulerService", "Job scheduling failed!");
        }
    }
}


