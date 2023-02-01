package com.sfc.myapplication;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.util.Log;

public class JobSchedulerExample {

    private static final int JOB_ID = 1;
    private static final String SERVER_URL = "https://ami.moe/";

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, NetworkJobService.class);
        PersistableBundle extras = new PersistableBundle();
        extras.putString("serverUrl", SERVER_URL);

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent);
        builder.setPeriodic(3 * 1000); // schedule job every 3sec
        builder.setExtras(extras);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        // Get an instance of the JobScheduler
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        // Create a new JobInfo object and set the requirements
        JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context, WifiScanService.class))
                .setPeriodic(3000) // set the period to 3 seconds
                .build();

        // Schedule the job
        jobScheduler.schedule(jobInfo);

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(builder.build());
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d("JobSchedulerExample", "Job scheduled successfully!");
        } else {
            Log.d("JobSchedulerExample", "Failed to schedule job.");
        }
    }

    public static void cancelJob(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(JOB_ID);
        Log.d("JobSchedulerExample", "Job canceled successfully!");
    }

    public static class NetworkJobService extends JobService {
        @Override
        public boolean onStartJob(JobParameters params) {
            String serverUrl = params.getExtras().getString("serverUrl");
            new NetworkTask().execute(serverUrl);
            return true;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            return false;
        }

        private class NetworkTask extends AsyncTask<String, Void, Void> {
            @Override
            protected Void doInBackground(String... params) {
                String serverUrl = params[0];
                // Perform GET request to serverUrl
                // ...
                // Perform GET request to serverUrl/flag
                // ...
                return null;
            }
        }
    }
}
