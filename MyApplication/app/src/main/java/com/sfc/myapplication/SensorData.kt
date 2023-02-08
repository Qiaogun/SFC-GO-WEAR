package com.sfc.myapplication

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.io.StringWriter
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.*



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

//    private val skinTmp: Sensor by lazy {
//        mSensorManager.getDefaultSensor(69686)
//    }
//    private val sSPO2: Sensor by lazy {
//        mSensorManager.getDefaultSensor(69678)
//    }

    private val mSensor: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    private val channel = Channel<Pair<Int, FloatArray>>()

    private var job: Job? = null

    init {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_NORMAL);
//        mSensorManager.registerListener(this, skinTmp, SensorManager.SENSOR_DELAY_NORMAL);
//        mSensorManager.registerListener(this, sSPO2, SensorManager.SENSOR_DELAY_NORMAL);


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
        val currentTimestamp = Instant.now().toEpochMilli()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedTimestamp = Instant.ofEpochMilli(currentTimestamp).atZone(java.time.ZoneId.systemDefault()).format(formatter)
        //println(formattedTimestamp)
        val stringWriter = StringWriter()
        var postdata: String? = ""
        val type = event.sensor.type
        if (type == 1)//TYPE_ACCELEROMETER
        {   val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            //Log.d("onSensorChanged $type ${event.sensor}", "$x, $y, $z")
            stringWriter.write(
                kotlin.String.format(
                    "{\"TYPE\": \"%s\", \"Time\": \"%s\", \"x\": %s, \"y\": %s, \"z\": %s}", "TYPE_ACCELEROMETER", formattedTimestamp, x, y, z)
            )
            jsonPost(stringWriter.toString())
            Log.d("onSensorChanged $type TYPE_ACCELEROMETER", "$x, $y, $z")

        }
        else if(type == 6)//TYPE_PRESSURE
        {val x: Float = event.values[0]
            stringWriter.write(
                kotlin.String.format(
                    "{\"TYPE\": \"%s\", \"Time\": \"%s\", \"x\": %s}", "TYPE_PRESSURE", formattedTimestamp, "$x")
            )
            jsonPost(stringWriter.toString())
            Log.d("onSensorChanged $type TYPE_PRESSURE", "$x")}
        else if (type == 9) //TYPE_GRAVITY
        {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            stringWriter.write(
                kotlin.String.format(
                    "{\"TYPE\": \"%s\", \"Time\": \"%s\", \"x\": \"%s\", \"y\": \"%s\", \"z\": \"%s\"}", "TYPE_GRAVITY", formattedTimestamp ,"$x, $y, $z")
            )
            jsonPost(stringWriter.toString())
            Log.d("onSensorChanged $type TYPE_GRAVITY", "$x, $y, $z")
        }
        else if(type == 18)//TYPE_STEP_COUNTER
        {val x: Float = event.values[0]
            stringWriter.write(
                kotlin.String.format(
                    "{\"TYPE\": \"%s\", \"Time\": \"%s\", \"x\": %s}", "TYPE_STEP_COUNTER",formattedTimestamp, x)
            )
            jsonPost(stringWriter.toString())
            Log.d("onSensorChanged $type TYPE_STEP_COUNTER", "$x")}

        else if(type == 21)//HeartRate
        {val x : Float= event.values[0]
            stringWriter.write(
                kotlin.String.format(
                    "{\"TYPE\": \"%s\", \"Time\": \"%s\", \"x\": %s}", "TYPE_HEART_RATE",formattedTimestamp, "$x")
            )
            jsonPost(stringWriter.toString())
            Log.d("onSensorChanged $type TYPE_HEART_RATE", "$x")}

        else if(type == 5)//light
        {val x: Float = event.values[0]
            stringWriter.write(
                kotlin.String.format(
                    "{\"TYPE\": \"%s\", \"Time\": \"%s\", \"x\": %s}", "TYPE_LIGHT", formattedTimestamp,"$x")
            )
            jsonPost(stringWriter.toString())
            Log.d("onSensorChanged $type TYPE_LIGHT", "$x")}

        else if(type == 69686)//tempr
        {val x: Float = event.values[0]
            Log.d("onSensorChanged $type TYPE_tem", "$x")}

        else if(type == 69678)//spo2
        {val x: Float = event.values[0]
            Log.d("onSensorChanged $type TYPE_SPO2", "$x")}

        val data = event.values.clone()
        CoroutineScope(Dispatchers.IO).launch {
            channel.send(Pair<Int, FloatArray>(type, data))
        }
    }
    fun posttonet(postdata : String){
        var conn: HttpURLConnection? = null
        try
        {
            Log.d("TYPE", "sensor")
            //var URL url = new URL("http://192.168.88.24:23333/GPS");
            val url = URL("http://172.20.10.3:23333/")
            //val url = URL("http://43.206.213.194:23333/")
            conn = url.openConnection() as HttpURLConnection
            conn!!.requestMethod = "POST"
            conn!!.doOutput = true
            conn!!.useCaches = false
            conn!!.connectTimeout = 5000
            conn!!.readTimeout = 5000
            // 发送data
            val data = postdata.toByteArray(charset("utf-8"))
            conn!!.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn!!.setRequestProperty("Content-Length", "" + data.size)
            val outputStream = conn!!.outputStream
            outputStream.write(data)
            outputStream.close()
            conn!!.responseCode
        }catch (  e:java.net.MalformedURLException)
        {
            //throw new RuntimeException(e);
        }catch (  e:java.io.IOException)
        {
            //throw new RuntimeException(e);
        }finally
        {
            if (conn != null) {
                conn!!.disconnect()
            }
        }
    }
    fun jsonPost(jsonBody: String) {
        GlobalScope.launch(Dispatchers.Default) {

            val url = URL("http://172.20.10.3:23333/")

            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                connection.doOutput = true
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.outputStream.bufferedWriter().use {
                    it.write(jsonBody)
                }
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use {
                        println("Response: ${it.readText()}")
                    }
                }
            }catch (  e:java.net.MalformedURLException)
        {
            //throw new RuntimeException(e);
        }catch (  e:java.io.IOException)
        {
            //throw new RuntimeException(e);
        }finally
                {
                    connection.disconnect()
                }
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

