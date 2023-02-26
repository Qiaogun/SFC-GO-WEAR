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
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WifiScanTask extends AsyncTask<Void, Void, Void> {

    private static Context context;
    //private static List WiFiresjson;
    private static final String TAG = "WifiScanTask";
    Context mcontext;


    static class HttpTask extends AsyncTask<List<ScanResult>, Void, Integer> {

        @Override
        protected Integer doInBackground(List<ScanResult>... lists) {
            SharedPreferences sharedPref = context.getSharedPreferences("MAIN_DATA", MODE_PRIVATE);
            SharedPreferences sharedPrefid = context.getSharedPreferences("button_state", MODE_PRIVATE);
            String savedUsername = sharedPrefid.getString("username","");
            String selectedteam = sharedPrefid.getString("selectedteam","");
            String savedDeviceUUID = sharedPref.getString("deviceUUID","");
            ArrayList WiFiresjson = new ArrayList<String>();;
            HttpURLConnection conn = null;
            try {
                //Log.d(TAG, "doInBackground");

                //URL url = new URL("http://192.168.88.24:23333/WiFi");
                URL url = new URL("http://43.206.213.194:23333/WiFi");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                // 发送data
                StringWriter stringWriter = new StringWriter();
                stringWriter.write(String.format("{\"DeviceUUID\": \"%s\", \"Username\": \"%s\", \"TeamType\": \"%s\", \"WifiList\": ",savedDeviceUUID,savedUsername,selectedteam));
                for (ScanResult result : lists[0]) {
                    if (result.level > -67) {
                        //Log.d(TAG, String.format("\"SSID\": \"%s\", \"BSSID\": \"%s\", \"level\": %d ", result.SSID, result.BSSID, result.level));
                        WiFiresjson.add(String.format("{"+"\"SSID\": \"%s\", \"BSSID\": \"%s\", \"Level\": %d }", result.SSID, result.BSSID, result.level));
                    }
                }
                String res= String.join(",",WiFiresjson);
                //Log.d(TAG,res);
                stringWriter.write("["+res+"]}");

                //Log.d(TAG,stringWriter.toString());
                byte[] data = stringWriter.toString().getBytes("utf-8");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", "" + data.length);

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(data);
                outputStream.close();

                //Log.d(TAG, "send final");
                return conn.getResponseCode();
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

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

//            Toast.makeText(context, String.valueOf(integer), Toast.LENGTH_SHORT).show();
        }
    }


    private int interval = 90 * 1000; // 扫描间隔时间，单位为毫秒 90 * 1000 1，5min
    private boolean isRunning = true;

    private WifiManager wifiManager;



    public WifiScanTask(Context context)
    {

        this.context = context;
        mcontext  = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    //Log.d(TAG, "Scan success");
                    //showToast("WiFi Scan success");
                    List<ScanResult> results = wifiManager.getScanResults();
//                    for (ScanResult scanResult : results) {
//                        if (scanResult.level > -67) {
//                            Log.d(TAG, "SSID: " + scanResult.SSID + " BSSID: " + scanResult.BSSID + " level: " + scanResult.level);
//                        }
//                    }

                    try {
                        new HttpTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, results);
                        //Log.d(TAG, "Scan final");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Log.e(TAG, "Scan fail!!!");
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
            //Log.d(TAG, "Scan running");
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
    private void showToast(String message) {
        Context context = mcontext;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }



    private void scanWiFi() {
        //Log.d(TAG, "startScan");
        boolean success = wifiManager.startScan();
        if (!success) {
            Log.e(TAG, "startScan error");
        }
    }


    public void stop() {
        isRunning = false;
    }


}