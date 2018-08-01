package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.lib.data.Complex
import team_emergensor.co.jp.emergensor.lib.data.Message
import team_emergensor.co.jp.emergensor.lib.filter.base.FunctionFilter
import team_emergensor.co.jp.emergensor.lib.function.Utils

/**
 * Created by koichihasegawa on 2018/07/20.
 */
class ComplexNormFunctionFilter : FunctionFilter<Array<Complex>, Double>() {
    override fun getResult(body: Message.Body<Array<Complex>>): Double {
        return Utils.getComplexNorm(body.data)
    }
}