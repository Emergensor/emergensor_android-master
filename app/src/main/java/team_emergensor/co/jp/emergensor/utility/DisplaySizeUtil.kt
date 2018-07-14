package team_emergensor.co.jp.emergensor.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.View


/**
 * Created by koichihasegawa on 2018/07/14.
 */
object DisplaySizeUtil {

    fun getDisplaySize(activity: Activity): Point {
        val display = activity.windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        return point
    }

    /**
     * Get a Real Size(Hardware Size)
     * @param activity
     * @return
     */
    @SuppressLint("NewApi")
    fun getRealSize(activity: Activity): Point {

        val display = activity.windowManager.defaultDisplay
        val point = Point(0, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Android 4.2~
            display.getRealSize(point)
            return point

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // Android 3.2~
            try {
                val getRawWidth = Display::class.java.getMethod("getRawWidth")
                val getRawHeight = Display::class.java.getMethod("getRawHeight")
                val width = getRawWidth.invoke(display) as Int
                val height = getRawHeight.invoke(display) as Int
                point.set(width, height)
                return point

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        return point
    }

    /**
     * Get a view size. if display view size, after onWindowFocusChanged of method
     * @param View
     * @return
     */
    fun getViewSize(View: View): Point {
        val point = Point(0, 0)
        point.set(View.getWidth(), View.getHeight())

        return point
    }
}