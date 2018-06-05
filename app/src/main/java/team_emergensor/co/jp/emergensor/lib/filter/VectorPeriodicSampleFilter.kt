package team_emergensor.co.jp.emergensor.lib.filter

import io.reactivex.Observable
import team_emergensor.co.jp.emergensor.lib.data.Message
import team_emergensor.co.jp.emergensor.lib.filter.base.Filter

class VectorPeriodicSampleFilter(private val period: Long) : Filter.FlatMap<Array<Double>, Array<Double>>() {

    private var lastMessage: Message<Array<Double>>? = null
    private var lastTime: Long = 0

    override fun filter(message: Message<Array<Double>>): Observable<Message<Array<Double>>> {
        return Observable.create<Message<Array<Double>>> {
            if (lastMessage == null) {
                lastMessage = message
                lastTime = message.timeStamp
                it.onNext(message)
                it.onComplete()
            } else {
                val lastMessage = lastMessage!!
                while (lastTime + period <= message.timeStamp) {
                    lastTime += period

                    val a = message.timeStamp - lastMessage.timeStamp
                    val b = lastTime - lastMessage.timeStamp
                    val ratio = b.toDouble() / a.toDouble()

                    it.onNext(Message(lastTime, Message.Body(lastMessage.body.size, message.body.periodicSampleFilter(ratio))))

                }
                this.lastMessage = message
                it.onComplete()
            }
        }
    }


    private fun Message.Body<Array<Double>>.periodicSampleFilter(ratio: Double): Array<Double> {
        val lastMessage = lastMessage!!

        return this.data.mapIndexed({ index, fr ->
            lastMessage.body.data[index] * (1 - ratio) + fr * ratio
        }).toTypedArray()
    }
}