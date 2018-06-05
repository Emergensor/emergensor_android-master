package team_emergensor.co.jp.emergensor.data.repository

import android.arch.persistence.room.Room
import android.content.Context
import es.dmoral.prefs.Prefs
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.data.local.FacebookDatabase
import team_emergensor.co.jp.emergensor.data.service.FacebookService
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriends
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo

class FacebookRepository(private val context: Context) {

    private val facebookInfoDao by lazy {
        Room.databaseBuilder(context, FacebookDatabase::class.java, "database-name").build().facebookMyInfoDao()
    }
    private val facebookService by lazy {
        FacebookService()
    }

    fun getFriends(): Single<Array<FacebookFriends>> =
            Single.create<Array<FacebookFriends>> {
                getMyInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe { info, e ->
                            e?.let { _e ->
                                it.onError(_e)
                                return@let
                            }
                            facebookService.getFriends(info)
                                    .observeOn(Schedulers.io())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe { result, e ->
                                        e?.let { _e ->
                                            it.onError(_e)
                                            return@subscribe
                                        }
                                        it.onSuccess(result)
                                    }
                        }
            }

    fun getMyInfo(): Single<MyFacebookInfo> = Single.create<MyFacebookInfo> {
        val data = facebookInfoDao.get()

        if (data == null) {
            facebookService.getMyInfo()
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe { info, e ->
                        e?.let { _e ->
                            it.onError(_e)
                            return@subscribe
                        }
                        facebookInfoDao.insert(info)
                        it.onSuccess(info)
                    }
        } else {
            it.onSuccess(data)
        }
    }

    fun isExistUserInFirebase(): Boolean {
        return Prefs.with(context).readBoolean(EXIST_IN_FIREBASE)
    }

    fun setExistingInFirebase(boolean: Boolean) {
        Prefs.with(context).writeBoolean(EXIST_IN_FIREBASE, boolean)
    }

    companion object {
        const val EXIST_IN_FIREBASE = "exist in firebase"
    }
}