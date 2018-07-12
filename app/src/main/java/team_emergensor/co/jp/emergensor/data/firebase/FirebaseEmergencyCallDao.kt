package team_emergensor.co.jp.emergensor.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import io.reactivex.Observable
import team_emergensor.co.jp.emergensor.domain.entity.AutoEmergencyCall
import team_emergensor.co.jp.emergensor.domain.entity.DangerousArea
import team_emergensor.co.jp.emergensor.domain.entity.EmergencyCall
import team_emergensor.co.jp.emergensor.domain.entity.EmergensorUser
import java.util.*

class FirebaseEmergencyCallDao() {
    private val db = FirebaseFirestore.getInstance()
    private val emergencyCallRef = db.collection("emergencyCall")
    private val autoEmergencyCallRef = db.collection("autoEmergencyCall")
    private val dangerousAreaRef = db.collection("dangerousArea")

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

    fun dangerousAreaSnapshot(): Observable<Array<DangerousArea>> {
        return Observable.create {
            dangerousAreaRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    it.onError(firebaseFirestoreException)
                } else {
                    val list = mutableListOf<DangerousArea>()
                    querySnapshot?.forEach {
                        list.add(DangerousArea(it["facebook_id"].toString(), it["picture"].toString(), it["description"].toString(), it["point"] as GeoPoint, it["date"] as @com.google.firebase.firestore.ServerTimestamp Date))
                    }
                    it.onNext(list.toTypedArray())
                }
            }
        }
    }
}