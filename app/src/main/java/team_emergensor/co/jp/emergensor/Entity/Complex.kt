package team_emergensor.co.jp.emergensor.Entity

/**
 * Created by koichihasegawa on 2018/05/27.
 */
class Complex(var re: Double, var im: Double) {

    fun abs(): Double {
        return Math.hypot(this.re, this.im)
    }

    fun phase(): Double {
        return Math.atan2(this.im, this.re)
    }

    fun conjugate(): Complex {
        return Complex(this.re, -this.im)
    }

    fun reciprocal(): Complex {
        val scale = this.re * this.re + this.im * this.im
        return Complex(this.re / scale, -this.im / scale)
    }

    operator fun plus(b: Complex): Complex {
        val a = this
        val real = a.re + b.re
        val imag = a.im + b.im
        return Complex(real, imag)
    }

    operator fun minus(b: Complex): Complex {
        val a = this
        val real = a.re - b.re
        val imag = a.im - b.im
        return Complex(real, imag)
    }

    operator fun times(b: Complex): Complex {
        val a = this
        val real = a.re * b.re - a.im * b.im
        val imag = a.re * b.im + a.im * b.re
        return Complex(real, imag)
    }

    operator fun times(alpha: Double): Complex {
        return Complex(alpha * this.re, alpha * this.im)
    }

    fun divides(b: Complex): Complex {
        val a = this
        return a.times(b.reciprocal())
    }

    override fun hashCode(): Int {
        var result = 1

        var temp = java.lang.Double.doubleToLongBits(this.im)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = java.lang.Double.doubleToLongBits(this.re)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as Complex?
        if (java.lang.Double.doubleToLongBits(this.im) != java.lang.Double.doubleToLongBits(other!!.im)) return false
        return java.lang.Double.doubleToLongBits(this.re) == java.lang.Double.doubleToLongBits(other.re)
    }
}