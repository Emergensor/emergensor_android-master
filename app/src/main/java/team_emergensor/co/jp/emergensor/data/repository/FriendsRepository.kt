package team_emergensor.co.jp.emergensor.data.repository

import android.content.Context
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import team_emergensor.co.jp.emergensor.data.service.FacebookService
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriend

class FriendsRepository(private val context: Context) : Repository(context) {

    private val facebookService by lazy {
        FacebookService()
    }
    private val myInfoRepository by lazy {
        MyInfoRepository(context)
    }

    private val compositeDisposable = CompositeDisposable()

    fun getFriends(): Single<Array<FacebookFriend>> {
        return myInfoRepository.getMyInfo().flatMap { info ->
            facebookService.getFriends(info)
        }
    }
}