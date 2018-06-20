package team_emergensor.co.jp.emergensor.data.firebase

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo
import team_emergensor.co.jp.emergensor.domain.entity.MyFirebaseUser


class FirebaseDao(private val firebaseUser: FirebaseUser) {
    private val followingRef = FirebaseDatabase.getInstance().getReference("following")
    private val followedRef = FirebaseDatabase.getInstance().getReference("follower")
    private val usersRef = FirebaseDatabase.getInstance().getReference("facebook_users")

    fun follow(uid: String) {
        followingRef.child(firebaseUser.uid).updateChildren(mapOf(Pair(uid, true)))
        followedRef.child(uid).updateChildren(mapOf(Pair(firebaseUser.uid, true)))
    }

    fun unfollow(uid: String) {
        followingRef.child(firebaseUser.uid).child(uid).removeValue()
        followedRef.child(uid).child(firebaseUser.uid).removeValue()
    }

    fun setMyFacebookInfo(myFacebookInfo: MyFacebookInfo) {
        val firebaseUser = MyFirebaseUser(firebaseUser.uid, myFacebookInfo.name, myFacebookInfo.pictureUrl)
        usersRef.child(myFacebookInfo.id).setValue(firebaseUser)
    }
}