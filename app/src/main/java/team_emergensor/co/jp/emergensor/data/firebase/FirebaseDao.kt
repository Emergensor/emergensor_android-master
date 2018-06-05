package team_emergensor.co.jp.emergensor.data.firebase

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo
import team_emergensor.co.jp.emergensor.domain.entity.MyFirebaseUser

/**
 * Created by koichihasegawa on 2018/06/06.
 */
class FirebaseDao(private val firebaseUser: FirebaseUser) {
    private val followingRef = FirebaseDatabase.getInstance().getReference("following")
    private val followedRef = FirebaseDatabase.getInstance().getReference("follower")
    private val usersRef = FirebaseDatabase.getInstance().getReference("facebook_users")

    fun follow(uid: String) {
        followingRef.child(firebaseUser.uid).updateChildren(mapOf(Pair(uid, true)))
        followedRef.child(uid).updateChildren(mapOf(Pair(firebaseUser.uid, true)))
    }

    fun unfollow(uid: String) {
        followingRef.child(firebaseUser.uid).updateChildren(mapOf(Pair(uid, false)))
        followedRef.child(uid).updateChildren(mapOf(Pair(firebaseUser.uid, false)))
    }

    fun setMyFacebookInfo(myFacebookInfo: MyFacebookInfo) {
        val firebaseUser = MyFirebaseUser(firebaseUser.uid, myFacebookInfo.name)
        usersRef.child(myFacebookInfo.id).setValue(firebaseUser)
    }

    fun isExistsMyFacebookInfo(): Single<Boolean> {
        return Single.create {
            usersRef.child(firebaseUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    it.onError(Throwable(p0?.message))
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0 == null) {
                        it.onError(Throwable("firebase error isExistsMyFacebookInfo"))
                        return
                    }
                    it.onSuccess(p0.exists())
                }
            })
        }
    }
}