package team_emergensor.co.jp.emergensor.lib.filter.base

import team_emergensor.co.jp.emergensor.lib.data.Message

abstract class FunctionFilter<T, O>(private val resultBodySize: Int = 1) : Filter.Map<T, O>() {

    abstract fun getResult(body: Message.Body<T>): O

    private fun getBody(body: Message.Body<T>): Message.Body<O> {
        return Message.Body(resultBodySize, getResult(body))
    }

    override fun filter(message: Message<T>): Message<O> {
        return Message(message.timeStamp, getBody(message.body))
    }
}