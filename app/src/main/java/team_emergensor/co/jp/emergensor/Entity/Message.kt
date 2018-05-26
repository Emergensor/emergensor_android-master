package team_emergensor.co.jp.emergensor.Entity


class Message<out T>(val timeStamp: Long, val body: Body<T>) {
    class Body<out T>(val size: Int = 1, val data: T)
}