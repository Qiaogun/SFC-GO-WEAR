package com.sfc.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class FuseDataHandler {
    private LocationCallback mLocationCallback;
    private Executor executor;
    private Location mLocation;
    private String TAG = "fuseDataHandler";
    private Context mContext;
    private String savedDeviceUUID;
    private String savedUsername;
    private String savedTeam;
    private String dateString;
    private FusedLocationProviderClient mfusedLocationClient;

    public FuseDataHandler(FusedLocationProviderClient fusedLocationProviderClient,Context context) {
        mfusedLocationClient = fusedLocationProviderClient;
        mContext = context;
        SharedPreferences sharedPref = context.getSharedPreferences("MAIN_DATA", MODE_PRIVATE);
        SharedPreferences sharedPrefid = context.getSharedPreferences("button_state", MODE_PRIVATE);
        int selectedButtonId = sharedPrefid.getInt("selected_button_id", -1);
        savedTeam = sharedPrefid.getString("selectedteam", "");
        savedUsername = sharedPrefid.getString("username", "");
        savedDeviceUUID = sharedPref.getString("deviceUUID", "");
        this.mContext = context;
        executor = Executors.newFixedThreadPool(3);
    }

    private void showToast(String message) {
        Context context = mContext;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public void start() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            long timestamp = System.currentTimeMillis(); // assuming this is your timestamp
                            Date date = new Date(timestamp); // convert timestamp to Date object

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // create date format
                            dateString = dateFormat.format(date);
                            mLocation = location;
                            sendGPSData(mLocation);
                            //showToast("GPS Updated");
//                            showToast(""+mLocation.getLatitude()+mLocation.getLongitude());

                        }
                    }
                    // TODO: 处理位置回调结果
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            showToast("No permission");
        }

        LocationRequest mLocationRequest = new LocationRequest();
        // 设置位置更新的间隔（单位：毫秒）
        mLocationRequest.setInterval(10000);
        // 设置定位类型
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // 设置回调次数为1
        //mLocationRequest.setNumUpdates(1);
        mfusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // TODO: 接口调用成功的处理

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // TODO: 接口调用失败的处理
                    }
                });
    }


    public void stop() {
//        mlocationManager.removeUpdates(mlocationListener);
    }

    public void sendGPSData(Location location) {
        String data = String.format("{\"TimeStamp\":\"%s\", \"DeviceUUID\": \"%s\", \"Username\": \"%s\",\"TeamType\": \"%s\", \"Latitude\": %s, \"Longitude\": %s, \"Altitude\": %s, \"Accuracy\": %s, \"Speed\": %s, \"Bearing\": %s}", dateString,savedDeviceUUID,savedUsername,savedTeam, location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy(), location.getSpeed(), location.getBearing());

        //Log.d(TAG, data);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://43.206.213.194:23333/GPS");
//                    URL url = new URL("http://192.168.88.11:23333/GPS");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    OutputStream os = connection.getOutputStream();
                    os.write(data.getBytes());
                    os.flush();
                    os.close();
                    InputStream is = connection.getInputStream();

//                    StringWriter writer = new StringWriter();
//                    //IOUtils.copy(is, writer, "UTF-8");
//                    String response = writer.toString();
//                    Log.d(TAG, response);
                } catch (MalformedURLException e) {
                    //e.printStackTrace();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        });
    }

}

