package com.sfc.myapplication;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

import static androidx.core.app.ServiceCompat.START_STICKY;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GPSDataHandler {

    private LocationManager mlocationManager;
    private LocationListener mlocationListener;
    private Executor executor;
    private String TAG = "GPSDataHandler";
    private Context mContext;
    private String savedDeviceUUID;
    private String savedUsername;
    public GPSDataHandler(LocationManager locationManager, Context context) {
        mContext = context;
        SharedPreferences sharedPref = context.getSharedPreferences("MAIN_DATA", MODE_PRIVATE);
        SharedPreferences sharedPrefid = context.getSharedPreferences("button_state", MODE_PRIVATE);
        int selectedButtonId = sharedPrefid.getInt("selected_button_id", -1);
        savedUsername = sharedPrefid.getString("username", "");
        savedDeviceUUID = sharedPref.getString("deviceUUID", "");
        this.mlocationManager = locationManager;
        this.mContext = context;
        executor = Executors.newFixedThreadPool(1);
    }
    private void showToast(String message) {
        Context context = mContext;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
    public void start() {
        mlocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                showToast("GPS Updated");
                sendGPSData(location);
            }};
        if (ActivityCompat.checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             showToast("No permission");
        }
        mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mlocationListener);
    }
    public void stop() {
        mlocationManager.removeUpdates(mlocationListener);
    }

    public void sendGPSData(Location location) {
        String data = String.format("{\"DeviceUUID\": \"%s\", \"Username\": \"%s\", \"Latitude\": %s, \"Longitude\": %s, \"Altitude\": \"%s\", \"Accuracy\": \"%s\", \"Speed\": \"%s\", \"Bearing\": \"%s\"}", savedDeviceUUID,savedUsername, location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy(), location.getSpeed(), location.getBearing());
        Log.d(TAG, data);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://43.206.213.194:23333/gps");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    OutputStream os = connection.getOutputStream();
                    os.write(data.getBytes());
                    os.flush();
                    os.close();
                    InputStream is = connection.getInputStream();
                    StringWriter writer = new StringWriter();
                    //IOUtils.copy(is, writer, "UTF-8");
                    String response = writer.toString();
                    Log.d(TAG, response);
                } catch (MalformedURLException e) {
                    //e.printStackTrace();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        });
    }

}

