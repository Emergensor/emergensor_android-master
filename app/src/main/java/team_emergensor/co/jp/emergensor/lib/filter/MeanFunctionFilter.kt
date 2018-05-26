package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.filter.base.FunctionFilter

/**
 * Created by koichihasegawa on 2018/05/26.
 */
class MeanFunctionFilter : FunctionFilter<Array<Double>, Double>() {
    override fun getResult(body: Message.Body<Array<Double>>): Double {
        var a = 0.0
        body.data.forEach { a += it }
        a /= body.size
        return a
    }
}