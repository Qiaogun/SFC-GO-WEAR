package com.sfc.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class WifiScanTask extends AsyncTask<Void, Void, Void> {

    private static Context context;

    private static final String TAG = "WifiScanTask";
    static class HttpTask extends AsyncTask<List<ScanResult>, Void, Void> {

        ;

        @Override
        protected Void doInBackground(List<ScanResult>... lists) {
            SharedPreferences sharedPref = context.getSharedPreferences("button_state", MODE_PRIVATE);
            int selectedButtonId = sharedPref.getInt("selected_button_id", -1);
            String savedUsername = sharedPref.getString("username","");

            HttpURLConnection conn = null;
            try {
                Log.d(TAG, "doInBackground");

                URL url = new URL("http://192.168.88.24:23333/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                // 发送data
                StringWriter stringWriter = new StringWriter();
                stringWriter.write(String.format("username: %s","selected_button_id: %d\n",savedUsername,selectedButtonId));
                for (ScanResult result : lists[0]) {
                    if (result.level > -67) {
                        Log.d(TAG, String.format("SSID: %s, BSSID: %s, level: %d", result.SSID, result.BSSID, result.level));
                        stringWriter.write(String.format("\"SSID\": \"%s\", \"BSSID\": \"%s\", \"level\": %d\n", result.SSID, result.BSSID, result.level));
                    }
                }
                Log.d(TAG,stringWriter.toString());
                byte[] data = stringWriter.toString().getBytes("utf-8");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", "" + data.length);

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(data);
                outputStream.close();

                conn.getResponseCode();

                Log.d(TAG, "send final");
            } catch (MalformedURLException e) {
                //throw new RuntimeException(e);

            } catch (IOException e) {
                //throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }
    }


    private int interval = 5 * 1000; // 扫描间隔时间，单位为毫秒 90 * 1000 1，5min
    private boolean isRunning = true;

    private WifiManager wifiManager;



    public WifiScanTask(Context context) {

        this.context = context;

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    Log.d(TAG, "Scan success");
                    List<ScanResult> results = wifiManager.getScanResults();
//                    for (ScanResult scanResult : results) {
//                        if (scanResult.level > -67) {
//                            Log.d(TAG, "SSID: " + scanResult.SSID + " BSSID: " + scanResult.BSSID + " level: " + scanResult.level);
//                        }
//                    }

                    try {
                        new HttpTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, results);
                        Log.d(TAG, "Scan final");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Scan fail!!!");
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);
    }

    @Nullable
    @Override
    protected Void doInBackground(Void... voids) {
        while (isRunning) {
            // 在这里进行WiFi扫描
            Log.d(TAG, "Scan running");
            scanWiFi();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Toast.makeText(this, "WiFi扫描任务已结束", Toast.LENGTH_SHORT).show();
    }


    @Nullable
    private void scanWifi() {
//        boolean success = mwifiManager.startScan();
//        if (success) {
//            Log.d(TAG, "Scan started successfully!");
//            results = mwifiManager.getScanResults();
//            for (ScanResult scanResult : results) {
//                if (scanResult.level > -67) {
//                    Log.d(TAG, "SSID: " + scanResult.SSID + " BSSID: " + scanResult.BSSID + " level: " + scanResult.level);
//                }
//            }
//        } else {
//            Log.d(TAG, "Scan start failed!");
//        }


    }

    private void scanWiFi() {
        Log.d(TAG, "startScan");
        boolean success = wifiManager.startScan();
        if (!success) {
            Log.e(TAG, "startScan error");
        }
    }


    public void stop() {
        isRunning = false;
    }


}