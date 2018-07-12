package team_emergensor.co.jp.emergensor.data.repository

import android.content.Context
import team_emergensor.co.jp.emergensor.data.firebase.FirebaseEmergencyCallDao

class DangerousAreaRepository(private val context: Context): Repository(context) {
    private val firebaseEmergencyCallDao = FirebaseEmergencyCallDao()

    fun observeDangerousArea() = firebaseEmergencyCallDao.dangerousAreaSnapshot()
}