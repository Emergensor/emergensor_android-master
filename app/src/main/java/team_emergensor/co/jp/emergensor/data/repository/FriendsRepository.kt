package team_emergensor.co.jp.emergensor.data.repository

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import team_emergensor.co.jp.emergensor.data.firebase.FirebaseFacebookUserDao
import team_emergensor.co.jp.emergensor.data.service.FacebookService
import team_emergensor.co.jp.emergensor.domain.entity.FollowingUser

class FriendsRepository(private val context: Context) : Repository(context) {

    private val facebookService by lazy {
        FacebookService()
    }
    private val myInfoRepository by lazy {
        EmergensorUserRepository(context)
    }

    private val firebaseDao by lazy {
        FirebaseFacebookUserDao()
    }

    private val compositeDisposable = CompositeDisposable()

    fun registerFacebookFriends(): Single<Unit> {
        val info = myInfoRepository.getMyInfoLocal()
        return facebookService.getFriends(info).flatMap {
            firebaseDao.addFacebookUsers(info, it)
        }
    }

    fun observeFollowing(): Observable<Array<FollowingUser>> {
        val info = myInfoRepository.getMyInfoLocal()
        return firebaseDao.observeFollowing(info)
    }

    fun follow(uid: String, isFollow: Boolean) {
        val info = myInfoRepository.getMyInfoLocal()
        firebaseDao.follow(info, uid, isFollow)
    }
}