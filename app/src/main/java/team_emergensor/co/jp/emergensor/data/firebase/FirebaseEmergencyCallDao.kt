package team_emergensor.co.jp.emergensor.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import team_emergensor.co.jp.emergensor.domain.entity.AutoEmergencyCall
import team_emergensor.co.jp.emergensor.domain.entity.EmergencyCall
import team_emergensor.co.jp.emergensor.domain.entity.EmergensorUser
import java.util.*

class FirebaseEmergencyCallDao() {
    private val db = FirebaseFirestore.getInstance()
    val emergencyCallRef = db.collection("emergencyCall")
    val autoEmergencyCallRef = db.collection("autoEmergencyCall")

    fun call(user: EmergensorUser, call: EmergencyCall) {
        emergencyCallRef.document(Calendar.getInstance().time.toString()).set(call)
                .addOnSuccessListener {
                }.addOnFailureListener {
                }
    }

    fun autoCall(user: EmergensorUser, call: AutoEmergencyCall) {
        autoEmergencyCallRef.document(Calendar.getInstance().time.toString()).set(call)
                .addOnSuccessListener {
                }.addOnFailureListener {
                }
    }
}