package team_emergensor.co.jp.emergensor.domain.entity

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class DangerousArea(val point: GeoPoint, @ServerTimestamp val date: Date)