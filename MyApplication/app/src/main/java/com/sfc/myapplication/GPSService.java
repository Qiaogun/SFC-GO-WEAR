package com.sfc.myapplication;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GPSService extends Service {
    String TAG = "GPS UPDATE NETWORK";
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        SharedPreferences sharedPref = getSharedPreferences("MAIN_DATA", MODE_PRIVATE);
        SharedPreferences sharedPrefid = getSharedPreferences("button_state", MODE_PRIVATE);
        String selectedteam = sharedPrefid.getString("selectedteam", "");
        String savedUsername = sharedPrefid.getString("username","");
        String savedDeviceUUID = sharedPref.getString("deviceUUID","");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double latitude = location.getLatitude();
                double longitude =  location.getLongitude();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("latitude", String.valueOf(latitude));
                editor.putString("longitude", String.valueOf(longitude));
                //Log.d(TAG, String.valueOf(latitude) + longitude);
                editor.apply();
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://192.168.88.24:23333/GPS");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    //Log.d("GPSService", "Latitude: " + latitude + ", Longitude: " +longitude);
                    // 发送data
                    StringWriter stringWriter = new StringWriter();
                    stringWriter.write(String.format("{\"DeviceUUID\": \"%s\", \"Username\": \"%s\", \"TeamType\": \"%s\",",savedDeviceUUID,savedUsername,selectedteam));
                    stringWriter.write(String.format("\"Latitude\": %s, \"Longitude\": %s}\n",String.valueOf(latitude),String.valueOf(longitude)));
                    //stringWriter.write(String.format("\"Latitude\": %.7f, \"Longitude\": %.7f   }\n",latitude,longitude));

                    Log.d(TAG,stringWriter.toString());
                    byte[] data = stringWriter.toString().getBytes("utf-8");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", "" + data.length);

                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(data);
                    outputStream.close();

                    conn.getResponseCode();

                    //Log.d(TAG, "send final");
                } catch (MalformedURLException e) {
                    //throw new RuntimeException(e);
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };
        // check permission
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_STICKY;
        }
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

