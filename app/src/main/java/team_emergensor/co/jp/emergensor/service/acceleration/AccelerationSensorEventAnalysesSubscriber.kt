package team_emergensor.co.jp.emergensor.service.acceleration

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.filter.*


class AccelerationSensorEventAnalysesSubscriber : SensorEventListener {

    private val compositeDisposable = CompositeDisposable()
    private var sendCount = 0

    /**
     * publish subject
     *
     */
    private val sensorEventSubject = PublishSubject.create<Message<Array<Double>>>()

    /**
     * filters
     */
    private val vectorPeriodicSampleFilter = VectorPeriodicSampleFilter(DATA_DURATION)
    private val normFunctionFilter = NormFunctionFilter()
    private val bufferFilter = BufferFilter(FFT_BUFFER_SIZE)
    private val meanFunctionFilter = MeanFunctionFilter()
    private val varianceFunctionFilter = VarianceFunctionFilter()
    private val hanningFunctionFilter = HanningFunctionFilter()
    private val fftFunctionFilter = FFTFunctionFilter()

    /**
     * init (subscribe data(subject))
     */
    init {
        val disposable = sensorEventSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMapEager {
                    vectorPeriodicSampleFilter.filter(it)
                }.map {
                    normFunctionFilter.filter(it)
                }.concatMap {
                    bufferFilter.filter(it)
                }.map {
                    arrayOf(
                            meanFunctionFilter.filter(it).body.data,
                            varianceFunctionFilter.filter(it).body.data,
                            fftFunctionFilter.filter(hanningFunctionFilter.filter(it))
                    )
                }.subscribe({
                    Log.d("sensor", "平均${it[0]}, 分散${it[1]}")
                }, {
                    Log.d("sensor", it.message)
                })
        compositeDisposable.add(disposable)
    }

    /**
     * event listener (listen sensor and publish data)
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val sensorEvent = event ?: return
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val body = Message.Body(3, arrayOf(sensorEvent.values[0] * 0.135, sensorEvent.values[1] * 0.135, sensorEvent.values[2] * 0.135))
            val message = Message(event.timestamp / 100, body)

            if (sendCount > DEBUG_MAX_SEND_COUNT - 1) return
            sensorEventSubject.onNext(message) // publish
            sendCount++
        }
    }

    fun dispose() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    companion object {
        const val DEBUG_MAX_SEND_COUNT = 10
        const val FFT_BUFFER_SIZE = 256
        const val DATA_DURATION: Long = 1 * 1000 * 1000 / 100
    }

}