package com.sfc.myapplication;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;

import java.util.List;

public class MainActivity extends FragmentActivity implements AmbientModeSupport.AmbientCallbackProvider {
    private SensorDataHandler mSensorDataHandler;

    private GPSDataHandler mLocationManager;

    private AmbientModeSupport.AmbientController ambientController;
    public String deviceUUID;

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new MyAmbientCallback();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        Intent intent = new Intent(MainActivity.this, GPSService.class);
//        startService(intent);

        LocationManager gpsManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager = new GPSDataHandler(gpsManager,getApplicationContext());
        mLocationManager.start();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorDataHandler = new SensorDataHandler(sensorManager, getApplicationContext());
        mSensorDataHandler.start();
//        Executor executor = Executors.newSingleThreadExecutor();
//        executor.execute(()->{
//            while(true){
//                // Do something in the background thread
//                NetworkThread thread = new NetworkThread();
//                thread.run();
//                try {
//                    Thread.sleep(50000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

        WifiScanTask wifiScanTask = new WifiScanTask(getApplicationContext());
        wifiScanTask.execute();

        deviceUUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("MyTag deviceUUID", deviceUUID);


        SharedPreferences sharedPref = getSharedPreferences("MAIN_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("deviceUUID", deviceUUID);
        Log.d("deviceUUID", deviceUUID);
        editor.apply();

        String currlatitude = sharedPref.getString("latitude", "");
        super.onCreate(savedInstanceState);

        ambientController = AmbientModeSupport.attach(this);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button_1);

//        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//
//        ComponentName componentName = new ComponentName(this, WifiScanService.class);
//        Log.d("MyTag ", "WIFI3s");
//        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
//        builder.setPeriodic(600000); //3s
//        builder.setPersisted(true);
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // any network
//        jobInfo = builder.build();

//        Intent intent = new Intent(MainActivity.this, WifiScanService.class);
//        startActivity(intent);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch ChildActivity1
                Intent intent = new Intent(MainActivity.this, TeamSelect.class);
                startActivity(intent);
            }
        });

        Button button2 = findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch ChildActivity2
                Intent intent = new Intent(MainActivity.this, FlagMaps.class);
                startActivity(intent);
            }
        });
        Button button3 = findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch ChildActivity3
                Intent intent = new Intent(MainActivity.this, FlagUI.class);
                startActivity(intent);
            }

        });
//        Button button4 = findViewById(R.id.button_4);
//        button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Launch ChildActivity3
//                SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//                List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
//                for (Sensor sensor : sensorList) {
//                    Log.d("SENSOR", "Name: " + sensor.getName() + ", Type: " + sensor.getType());
//                }
//            }
//        });
        Button button5 = findViewById(R.id.button_5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch ChildActivity5
                finish();
            }
        });
//        Button button6 = findViewById(R.id.button_6);
//        button6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Launch ChildActivity6
//                Log.d("MyTag latitude", currlatitude);
//                Intent intent = new Intent(MainActivity.this, GPSService.class);
//                startService(intent);
//
//            }
//
//        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorDataHandler.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorDataHandler.start();
    }
    ;

}

