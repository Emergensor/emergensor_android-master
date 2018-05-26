package team_emergensor.co.jp.emergensor.lib.filter.base

import io.reactivex.Observable
import team_emergensor.co.jp.emergensor.Entity.Message


sealed class Filter {
    abstract class Map<in T, out O> : Filter() {
        abstract fun filter(message: Message<T>): Message<O>
    }

    abstract class FlatMap<in T, O> : Filter() {
        abstract fun filter(message: Message<T>): Observable<Message<O>>
    }
}