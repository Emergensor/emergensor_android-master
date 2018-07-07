package team_emergensor.co.jp.emergensor.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import team_emergensor.co.jp.emergensor.data.firebase.FirebaseDao
import team_emergensor.co.jp.emergensor.data.service.FacebookService
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriend

class FriendsRepository(private val context: Context) : Repository(context) {

    private val facebookService by lazy {
        FacebookService()
    }
    private val myInfoRepository by lazy {
        MyInfoRepository(context)
    }

    private val firebaseDao by lazy {
        FirebaseDao(FirebaseAuth.getInstance().currentUser!!)
    }

    private val compositeDisposable = CompositeDisposable()

    fun getFriends(): Single<Array<FacebookFriend>> {
        return myInfoRepository.getMyInfo().flatMap { info ->
            facebookService.getFriends(info)
        }
    }

    fun follow(uid: String) {
        firebaseDao.follow(uid)
    }

    fun unfollow(uid: String) {
        firebaseDao.unfollow(uid)
    }
}