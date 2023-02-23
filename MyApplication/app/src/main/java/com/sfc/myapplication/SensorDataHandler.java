package com.sfc.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SensorDataHandler implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensorACC;
    private Sensor mSensorHR;
    private Sensor mSensorPRESS;
    private Sensor mSensorLIGHT;
    private Sensor mSensorSKINTEMP;
    private ExecutorService executor;
    String uuuid = "";
    private String url = "http://43.206.213.194:23333/sensor";
    //private String url = "http://192.168.88.24:23333/sensor";


    public SensorDataHandler(SensorManager sensorManager,Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MAIN_DATA", MODE_PRIVATE);
        SharedPreferences sharedPrefid = context.getSharedPreferences("button_state", MODE_PRIVATE);int selectedButtonId = sharedPrefid.getInt("selected_button_id", -1);
        String savedUsername = sharedPrefid.getString("username","");
        String savedDeviceUUID = sharedPref.getString("deviceUUID","");

        uuuid  = String.format("{\"DeviceUUID\": \"%s\", \"Username\": \"%s\", ",savedDeviceUUID,savedUsername);

        mSensorManager = sensorManager;
        mSensorACC = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorPRESS = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mSensorHR = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorLIGHT = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorSKINTEMP = mSensorManager.getDefaultSensor(69686);
        executor = Executors.newFixedThreadPool(3);
    }

    public void start() {
        mSensorManager.registerListener(this, mSensorACC, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorPRESS, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorHR, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorLIGHT, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorSKINTEMP, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
        executor.shutdown();
    }
    public void stopExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public void submitTask(Runnable task) {
        if (!executor.isShutdown()) {
            executor.submit(task);
        } else {
            // Handle the situation appropriately, for example start a new executor
            executor = Executors.newFixedThreadPool(2);

        }
    }

@Override
    public void onSensorChanged(SensorEvent event) {
    long currentTimestamp = Instant.now().toEpochMilli();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedTimestamp = Instant.ofEpochMilli(currentTimestamp).atZone(ZoneId.systemDefault()).format(formatter);
    String time = String.format("\"Timestamp\": \"%s\", ",formattedTimestamp);
    final float[] data = event.values.clone();
        StringWriter stringWriter = new StringWriter();

        if (event.sensor.getType()==1)
        {
            stringWriter.write(uuuid + time + "\"Accelerometer_x\": " + data[0] + ",\"Accelerometer_y\": " + data[1] + ",\"Accelerometer_z\": " + data[2]+"]}");
            //Log.d("MyTag", "Accelerometer data: " + data[0] + ", " + data[1] + ", " + data[2]);
        }
        if (event.sensor.getType()==6)
        {
            stringWriter.write(uuuid + time +"\"Pressure\": " + data[0]+"}");
            //Log.d("MyTag", "Pressure data: " + data[0]);
        }
        if (event.sensor.getType()==21)
        {
            stringWriter.write(uuuid + time +"\"HeartRate\": " + data[0]+"}");
            //Log.d("MyTag", "Pressure data: " + data[0]);
        }
        if (event.sensor.getType()==5){
            stringWriter.write(uuuid + time +"\"Light\": " + data[0]+"}");
        }
        if (event.sensor.getType()==69686){
            stringWriter.write(uuuid + time +"\"SkinTemp\": " + data[0]+"}");
        }
        String res = stringWriter.toString();
    if (!executor.isShutdown() && !executor.isTerminated()) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                sendSensorDataOverNetwork(res);
            }
        });
    }else {
        executor = Executors.newFixedThreadPool(2);
    }}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
    private  void sendSensorDataOverNetwork(String json) {
       // Log.d("MyTag", "Sending sensor data over network: " + json);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            //connection.connect();
            byte[] data = json.getBytes("utf-8");
            connection.setRequestProperty("Content-Length", "" + data.length);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();
            connection.getResponseCode();
           // Log.d("MyTag", "send final");

        } catch (MalformedURLException e) {
            //throw new RuntimeException(e);

        } catch (IOException e) {
            //throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        }
}

