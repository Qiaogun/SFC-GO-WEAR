package com.sfc.myapplication

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.provider.Settings.Secure
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel



class SensorData(activity: Activity) :SensorEventListener {

    private val mSensorManager: SensorManager by lazy {
        activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val accelerometer: Sensor by lazy {
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }
    private val pressure: Sensor by lazy {
        mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }//气压 有
    private val heartRate: Sensor by lazy {
        mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    }//气压 有

    private val mSensor: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    private val channel = Channel<Pair<Int, FloatArray>>()

    private var job: Job? = null

    init {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_NORMAL);

        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val (accelerometer, accelerometerData) = channel.receive()
                val (pressure, pressureData) = channel.receive()
                val (temperature, temperatureData) = channel.receive()
                val (type, data) = channel.receive()
//
//                sendSensorDataToServer(accelerometerData, temperatureData, pressureData)
//                updateUIFromServer()
                delay(6000)
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO 在传感器精度发生改变时做些操作，accuracy为当前传感器精度
        //大部分传感器会返回三个轴方向x,y,x的event值，值的意义因传感器而异
    }

    override fun onSensorChanged(event: SensorEvent) {
        //
        val type = event.sensor.type
        if (type == 1)//TYPE_ACCELEROMETER
        {   val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            //Log.d("onSensorChanged $type ${event.sensor}", "$x, $y, $z")
            Log.d("onSensorChanged $type TYPE_ACCELEROMETER", "$x, $y, $z")

        }
        else if(type == 6)//TYPE_PRESSURE
        {val x: Float = event.values[0]
            Log.d("onSensorChanged $type TYPE_PRESSURE", "$x")}
        else if (type == 9) //TYPE_GRAVITY
        {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            Log.d("onSensorChanged $type TYPE_GRAVITY", "$x, $y, $z")
        }
        else if(type == 18)//TYPE_STEP_COUNTER
        {val x: Float = event.values[0]
            Log.d("onSensorChanged $type TYPE_STEP_COUNTER", "$x")}

        else if(type == 21)//HeartRate
        {val x : Float= event.values[0]
            Log.d("onSensorChanged $type TYPE_HEART_RATE", "$x")}

        else if(type == 5)//light
        {val x: Float = event.values[0]
            Log.d("onSensorChanged $type TYPE_LIGHT", "$x")}

        else if(type == 69678)//tempr
        {val x: Float = event.values[0]
            Log.d("onSensorChanged $type TYPE_tem", "$x")}

        val data = event.values.clone()
        CoroutineScope(Dispatchers.IO).launch {
            channel.send(Pair<Int, FloatArray>(type, data))
        }
    }

//        override fun onPause() {
//        super.onPause()
//        mSensorManager.unregisterListener(this)
//    }
//    fun onPause() {
//        super.onPause()
//        mSensorManager.unregisterListener(this);
//        job?.cancel()
//    }
}

    /*
    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);

        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val (accelerometer, accelerometerData) = channel.receive()
                val (pressure, pressureData) = channel.receive()
                val (temperature, temperatureData) = channel.receive()
                val (type, data) = channel.receive()
//
//                sendSensorDataToServer(accelerometerData, temperatureData, pressureData)
//                updateUIFromServer()
                delay(6_000)
            }
        }
    }}

     */

