package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.filter.base.FunctionFilter

/**
 * Created by koichihasegawa on 2018/05/27.
 */
class HanningFunctionFilter : FunctionFilter<Array<Double>, Array<Double>>() {

    override fun getResult(body: Message.Body<Array<Double>>): Array<Double> {
        return body.data.mapIndexed { index, dbl ->
            dbl * getMultiplier(index, body.size)
        }.toTypedArray()
    }

    private fun getMultiplier(i: Int, size: Int): Double {
        return 0.5 - 0.5 * Math.cos(2.0 * Math.PI * i.toDouble() / (size - 1))
    }
}