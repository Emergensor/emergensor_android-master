package team_emergensor.co.jp.emergensor.lib.function;

object Utils {
    fun getNorm(data: Array<Double>): Double {
        var sum = 0.0
        data.forEach {
            sum += it * it
        }
        return Math.sqrt(sum)
    }
}