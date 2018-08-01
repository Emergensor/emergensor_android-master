package team_emergensor.co.jp.emergensor.utility

import java.util.*


object DateUtil {
    fun getDiffString(date1: Date, date2: Date): String {
        // 日付をlong値に変換します。
        val dateTimeTo = date2.time
        val dateTimeFrom = date1.time

        // 差分の時間を算出します。
        val dayDiff = (dateTimeTo - dateTimeFrom).toDouble() / 1000
        val minute = 60
        val hour = minute * 60
        val day = hour * 24

        val days = Math.floor(dayDiff / day).toInt()
        val hours = Math.floor(dayDiff / hour).toInt()
        val minutes = Math.floor(dayDiff / minute).toInt()
        return if (days > 0) {
            "$days days ago"
        } else if (hours > 0) {
            "$hours hours ago"
        } else {
            "$minutes minutes ago"
        }
    }
}