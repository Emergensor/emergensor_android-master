package team_emergensor.co.jp.emergensor.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo
import team_emergensor.co.jp.emergensor.domain.entity.MyFirebaseUser


class FirebaseDao(private val firebaseUser: FirebaseUser) {
    private val db = FirebaseFirestore.getInstance()

    fun follow(uid: String) {
        val map = HashMap<String, Any?>()
        map["uid"] = uid
        db.collection("users")
                .document(firebaseUser.uid)
                .collection("following")
                .document(uid)
                .set(map)
                .addOnSuccessListener { Log.d("follow", "success") }
                .addOnFailureListener { }
        map["uid"] = firebaseUser.uid
        db.collection("users")
                .document(uid)
                .collection("followed")
                .document(firebaseUser.uid)
                .set(map)
                .addOnSuccessListener { Log.d("follow", "success") }
                .addOnFailureListener { }
    }

    fun unfollow(uid: String) {
        db.collection("users")
                .document(firebaseUser.uid)
                .collection("following")
                .document(uid)
                .delete()
                .addOnSuccessListener { Log.d("follow", "success") }
                .addOnFailureListener { }
        db.collection("users")
                .document(uid)
                .collection("followed")
                .document(firebaseUser.uid)
                .set(firebaseUser.uid)
                .addOnSuccessListener { }
                .addOnFailureListener { Log.d("follow", "success") }
    }

    fun setMyFacebookInfo(myFacebookInfo: MyFacebookInfo) {
        val firebaseUser = MyFirebaseUser(firebaseUser.uid, myFacebookInfo.name, myFacebookInfo.pictureUrl)

        db.collection("users")
                .document(this.firebaseUser.uid)
                .set(firebaseUser)
                .addOnSuccessListener { }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

    }

    companion object {
        const val TAG = "firebase dao"
    }
}