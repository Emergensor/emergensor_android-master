package team_emergensor.co.jp.emergensor.service.acceleration

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log


class AccelerationSensorService : Service() {
    /**
     * variables
     */
    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val sensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private val accelerationSensorEventSubscriber = AccelerationSensorEventAnalysesSubscriber()

    /**
     * lifecycle methods
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleStart()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handleEnd()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /**
     * methods
     */
    private fun handleStart() {
        Log.d("sensor", "start")
        sensorManager.registerListener(accelerationSensorEventSubscriber, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    private fun handleEnd() {
        Log.d("sensor", "end")
        sensorManager.unregisterListener(accelerationSensorEventSubscriber)
        accelerationSensorEventSubscriber.dispose()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AccelerationSensorService::class.java)
        }
    }
}
