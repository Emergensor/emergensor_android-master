package team_emergensor.co.jp.emergensor.service.acceleration

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import es.dmoral.prefs.Prefs
import io.reactivex.disposables.CompositeDisposable
import team_emergensor.co.jp.emergensor.presentation.login.LoginActivity
import team_emergensor.co.jp.emergensor.presentation.settings.SettingsFragment


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

    private val compositeDisposable = CompositeDisposable()

    /**
     * lifecycle methods
     */
    @SuppressLint("NewApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleStart()

        val context = applicationContext
        val channelId = "emergensor analysys"
        val title = "accleration sensor logging"

        val intent = Intent(context, LoginActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context, SERVICE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)

            val notification = Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setContentText("to stop this, tap and edit setting")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build()

            startForeground(1, notification)
            return START_NOT_STICKY
        } else {
            return START_STICKY
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handleEnd()
        stopSelf()
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
        compositeDisposable.addAll(
                accelerationSensorEventSubscriber.actionPublisher
                        .subscribe {
                            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(INTENT_ACTION_UPLOAD).putExtra(ACTION_KEY, it))
                        },
                accelerationSensorEventSubscriber.fftPublisher
                        .subscribe {
                            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(INTENT_ACTION_UPLOAD).putExtra(FFT_KEY, it))
                        }
        )
    }

    private fun handleEnd() {
        Log.d("sensor", "end")
        sensorManager.unregisterListener(accelerationSensorEventSubscriber)
        accelerationSensorEventSubscriber.dispose()
        Prefs.with(applicationContext).writeBoolean(SettingsFragment.TAG, false)
        compositeDisposable.dispose()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AccelerationSensorService::class.java)
        }

        const val SERVICE_REQUEST_CODE = 100
        const val INTENT_ACTION_UPLOAD = "team_emergensor.co.jp.emergensor.service.acceleration.INTENT_ACTION_UPLOAD"
        const val ACTION_KEY = "action key"
        const val FFT_KEY = "fft key"
        const val START_FLG_KEY = "start flg key"
    }
}
