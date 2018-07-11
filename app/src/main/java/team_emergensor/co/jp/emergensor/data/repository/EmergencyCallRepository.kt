package team_emergensor.co.jp.emergensor.data.repository

import android.content.Context
import team_emergensor.co.jp.emergensor.data.firebase.FirebaseEmergencyCallDao
import team_emergensor.co.jp.emergensor.domain.entity.AutoEmergencyCall
import team_emergensor.co.jp.emergensor.domain.entity.EmergencyCall

class EmergencyCallRepository(private val context: Context) : Repository(context) {
    private val firebaseEmergencyCallDao = FirebaseEmergencyCallDao()

    fun call(emergencyCall: EmergencyCall) {
        firebaseEmergencyCallDao.call(getMyInfoLocal(), emergencyCall)
    }

    fun autoCall(autoEmergencyCall: AutoEmergencyCall) {
        firebaseEmergencyCallDao.autoCall(getMyInfoLocal(), autoEmergencyCall)
    }
}