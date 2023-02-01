package com.sfc.myapplication;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

public class WifiScanService extends JobService {
    private static final String TAG = "WifiScanService";

    private WifiManager wifiManager;
    public static List<ScanResult> results;
    @Override
    public boolean onStartJob(JobParameters params) {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        boolean success = wifiManager.startScan();
        if(success){
            Log.d(TAG, "Scan started successfully!");
            results = wifiManager.getScanResults();
//            for (ScanResult scanResult : results) {
//                if (scanResult.level > -67) {
//                    //Log.d(TAG, "SSID: " + scanResult.SSID + " BSSID: " + scanResult.BSSID + " level: " + scanResult.level);
//
//                }
//            }
        } else {
            Log.d(TAG, "Scan start failed!");
        }
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
