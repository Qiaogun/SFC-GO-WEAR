package com.sfc.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String deviceUUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("MyTag deviceUUID", deviceUUID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button_1);
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
        Button button3= findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch ChildActivity3
                Intent intent = new Intent(MainActivity.this, FlagStatus.class);
                startActivity(intent);
            }

        });
        Button button4= findViewById(R.id.button_4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch ChildActivity3
                SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
                for (Sensor sensor : sensorList) {
                    Log.d("SENSOR", "Name: " + sensor.getName() + ", Type: " + sensor.getType());
                }
            }
        });
        Button button5= findViewById(R.id.button_5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch ChildActivity5
                SensorData sensorData;
                sensorData = new SensorData(MainActivity.this);
            }
        });
    }

}