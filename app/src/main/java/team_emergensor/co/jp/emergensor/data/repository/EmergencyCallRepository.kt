package team_emergensor.co.jp.emergensor.data.repository

import android.content.Context
import android.location.Location
import com.google.firebase.firestore.GeoPoint
import team_emergensor.co.jp.emergensor.data.firebase.FirebaseEmergencyCallDao
import team_emergensor.co.jp.emergensor.domain.entity.AutoEmergencyCall
import team_emergensor.co.jp.emergensor.domain.entity.EmergencyCall
import team_emergensor.co.jp.emergensor.presentation.dialog.ReportDialogFragment
import java.util.*

class EmergencyCallRepository(private val context: Context) : Repository(context) {
    private val firebaseEmergencyCallDao = FirebaseEmergencyCallDao()

    fun call(report: ReportDialogFragment.Companion.Report, location: Location) {
        val user = getMyInfoLocal()
        val emergencyCall = EmergencyCall(user.id, user.pictureUrl, Calendar.getInstance().time, GeoPoint(location.latitude, location.longitude),report.description, report.type )
        firebaseEmergencyCallDao.call(getMyInfoLocal(), emergencyCall)
    }

    fun autoCall(autoEmergencyCall: AutoEmergencyCall) {
        firebaseEmergencyCallDao.autoCall(getMyInfoLocal(), autoEmergencyCall)
    }
}