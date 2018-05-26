package team_emergensor.co.jp.emergensor.Entity

class RingBuffer<T>(val size: Int) {

    val data = mutableListOf<T>()

    var latestIndex: Int = 0
    var isFilled = false

    fun append(body: T): List<T>? {
        if (isFilled) {
            data.removeAt(0)
            data.add(body)
            return data.toMutableList()
        } else {
            data.add(body)
            if (data.size == size) isFilled = true
            return null
        }
    }

}