package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.lib.data.Message
import team_emergensor.co.jp.emergensor.lib.filter.base.FunctionFilter
import team_emergensor.co.jp.emergensor.lib.function.Utils

/**
 * Created by koichihasegawa on 2018/05/25.
 */
class NormFunctionFilter : FunctionFilter<Array<Double>, Double>() {
    override fun getResult(body: Message.Body<Array<Double>>): Double {
        return Utils.getNorm(body.data)
    }
}