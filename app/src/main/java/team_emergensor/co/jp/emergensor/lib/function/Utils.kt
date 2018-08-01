package team_emergensor.co.jp.emergensor.lib.function;

import team_emergensor.co.jp.emergensor.lib.data.Complex

object Utils {
    fun getNorm(data: Array<Double>): Double {
        var sum = 0.0
        data.forEach {
            sum += it * it
        }
        return Math.sqrt(sum)
    }

    fun getComplexNorm(data: Array<Complex>): Double {
        var sum = 0.0
        data.forEach {
            sum += it.abs() * it.abs()
        }
        return Math.sqrt(sum)
    }
}