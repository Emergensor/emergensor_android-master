package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.filter.base.FunctionFilter

/**
 * Created by koichihasegawa on 2018/05/26.
 */
class VarianceFunctionFilter : FunctionFilter<Array<Double>, Double>() {
    override fun getResult(body: Message.Body<Array<Double>>): Double {
        var a2 = 0.0
        var a1 = 0.0
        body.data.forEach {
            a2 += it * it
            a1 += it
        }
        a2 /= body.size
        a1 /= body.size

        return a2 - a1 * a1
    }

}