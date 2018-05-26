package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.Entity.Complex
import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.filter.base.FunctionFilter
import team_emergensor.co.jp.emergensor.lib.function.FFT

/**
 * Created by koichihasegawa on 2018/05/27.
 */
class FFTFunctionFilter : FunctionFilter<Array<Double>, Array<Complex>>() {

    override fun getResult(body: Message.Body<Array<Double>>): Array<Complex> {

        val data = body.data.map {
            Complex(it, 0.0)
        }.toTypedArray()

        val fft = FFT(false)
        fft.data = data
        fft.execute()
        return fft.data
    }

}