package team_emergensor.co.jp.emergensor.domain.entity

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class EmergencyCall(
        val facebook_id: String,
        @ServerTimestamp val date: Date,
        val point: GeoPoint,
        val description: String,
        val type: EmergencyType
)