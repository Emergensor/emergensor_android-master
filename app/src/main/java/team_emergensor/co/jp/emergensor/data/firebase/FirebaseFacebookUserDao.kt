package team_emergensor.co.jp.emergensor.data.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Observable
import io.reactivex.Single
import team_emergensor.co.jp.emergensor.domain.entity.EmergensorUser
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriend
import team_emergensor.co.jp.emergensor.domain.entity.FollowingUser


class FirebaseFacebookUserDao() {
    private val db = FirebaseFirestore.getInstance()

    fun follow(emergensorUser: EmergensorUser, uid: String, isFollow: Boolean) {

        val followingMap = HashMap<String, Any?>()
        followingMap["follow"] = isFollow
        userRef.document(emergensorUser.id).collection("following").document(uid).update(followingMap)

        val followedMap = HashMap<String, Any?>()
        followedMap["followed"] = isFollow
        db.collection("users").document(uid).collection("following").document(emergensorUser.id).update(followedMap)

    }

    fun setMyFacebookInfo(emergensorUser: EmergensorUser) {
        db.collection("users")
                .document(emergensorUser.id)
                .set(emergensorUser)
                .addOnSuccessListener { }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    fun setMyNotificationIdToken(tokenId: String, emergensorUser: EmergensorUser): Single<Unit> {
        return Single.create<Unit> {
            db.collection("users")
                    .document(emergensorUser.id)
                    .update(mapOf(Pair("tokenId", tokenId)))
                    .addOnCompleteListener { Unit -> it.onSuccess(kotlin.Unit) }
                    .addOnFailureListener { e -> it.onError(e) }
        }
    }

    fun addFacebookUsers(emergensorUser: EmergensorUser, users: Array<FacebookFriend>): Single<Unit> {
        return Single.create<Unit> { result ->
            val task: Task<QuerySnapshot> = userRef.document(emergensorUser.id).collection("following").get().addOnCompleteListener { task ->
                if (task.exception != null) {
                    result.onError(Throwable())
                    return@addOnCompleteListener
                }
                val qsp = task.result
                val notExistUsers = users.filterNot { user -> qsp.map { it.id }.contains(user.id) }
                notExistUsers.forEach {
                    val map = HashMap<String, Any?>()
                    map["facebook_id"] = it.id
                    map["name"] = it.name
                    map["picture"] = it.picture
                    map["follow"] = false
                    db.collection("users").document(it.id).collection("following").document(emergensorUser.id).get().addOnCompleteListener { task ->
                        if (task.exception != null) {
                            result.onError(Throwable())
                            return@addOnCompleteListener
                        }
                        val document = task.result
                        map["followed"] = if (document.exists()) {
                            document.get("follow") as Boolean
                        } else {
                            false
                        }
                        userRef.document(emergensorUser.id).collection("following").document(it.id).set(map)
                    }
                }
                result.onSuccess(Unit)
            }
        }
    }

    fun observeFollowsAndNotFollows(emergensorUser: EmergensorUser): Observable<Pair<Array<FollowingUser>, Array<FollowingUser>>> {
        return Observable.create<Pair<Array<FollowingUser>, Array<FollowingUser>>> {
            userRef.document(emergensorUser.id).collection("following").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    it.onError(firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val qsp = querySnapshot ?: return@addSnapshotListener
                val mutableListFollowing = qsp.documents.filter { it["follow"] as Boolean }.mapNotNull {
                    FollowingUser(it["facebook_id"].toString(), it["name"].toString(), it["picture"].toString(), it["follow"] as Boolean, it["followed"] as Boolean)
                }
                val mutableListNotFollowing = qsp.documents.filter { !(it["follow"] as Boolean) }.mapNotNull {
                    FollowingUser(it["facebook_id"].toString(), it["name"].toString(), it["picture"].toString(), it["follow"] as Boolean, it["followed"] as Boolean)
                }
                it.onNext(Pair(mutableListFollowing.toTypedArray(), mutableListNotFollowing.toTypedArray()))
            }

        }
    }

    fun observeFollowing(emergensorUser: EmergensorUser): Observable<Array<FollowingUser>> {
        return Observable.create<Array<FollowingUser>> {
            userRef.document(emergensorUser.id).collection("following").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    it.onError(firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val qsp = querySnapshot ?: return@addSnapshotListener
                val mutableListFollowing = qsp.documents.mapNotNull {
                    val hoge = it
                    FollowingUser(it["facebook_id"].toString(), it["name"].toString(), it["picture"].toString(), it["follow"] as Boolean, it["followed"] as Boolean)
                }
                it.onNext(mutableListFollowing.toTypedArray())
            }
        }
    }

    private val userRef = db.collection("users")

    companion object {
        const val TAG = "firebase dao"
    }
}