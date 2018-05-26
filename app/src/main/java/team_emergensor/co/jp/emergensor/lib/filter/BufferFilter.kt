package team_emergensor.co.jp.emergensor.lib.filter

import io.reactivex.Observable
import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.Entity.RingBuffer
import team_emergensor.co.jp.emergensor.lib.filter.base.Filter


class BufferFilter(private val size: Int) : Filter.FlatMap<Double, Array<Double>>() {

    private val data = RingBuffer<Double>(size)
    private val timestamps = LongArray(size, { _ -> 0 })

    override fun filter(message: Message<Double>): Observable<Message<Array<Double>>> {
        return Observable.create<Message<Array<Double>>> {
            timestamps[data.latestIndex] = message.timeStamp
            val buffer = data.append(message.body.data)?.toTypedArray()
            if (data.isFilled && buffer != null) {
                it.onNext(
                        Message(
                                timestamps[(data.latestIndex + 1) % size],
                                Message.Body(data.size, buffer)
                        )
                )
            }
            it.onComplete()
        }
    }

}