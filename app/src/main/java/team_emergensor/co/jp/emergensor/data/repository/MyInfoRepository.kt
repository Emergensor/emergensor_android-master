package team_emergensor.co.jp.emergensor.data.repository

import android.content.Context
import es.dmoral.prefs.Prefs
import io.reactivex.Single
import team_emergensor.co.jp.emergensor.data.service.FacebookService
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo

class MyInfoRepository(private val context: Context) : Repository(context) {

    private val facebookService by lazy {
        FacebookService()
    }

    fun setMyInfo(myFacebookInfo: MyFacebookInfo) {
        Prefs.with(context).write(ID_KEY, myFacebookInfo.id)
        Prefs.with(context).write(NAME_KEY, myFacebookInfo.name)
        Prefs.with(context).write(PIC_KEY, myFacebookInfo.pictureUrl)
    }

    fun getMyInfo(): Single<MyFacebookInfo> {
        val id = Prefs.with(context).read(ID_KEY, "")
        val name = Prefs.with(context).read(NAME_KEY, "")
        val picUrl = Prefs.with(context).read(PIC_KEY, "")

        return if (id.isNotEmpty() and name.isNotEmpty()) {
            Single.fromCallable { MyFacebookInfo(id, name, picUrl) }
        } else {
            facebookService.getMyInfo().flatMap {
                setMyInfo(it)
                Single.fromCallable { it }
            }
        }
    }

    fun isExistUserInFirebase(): Boolean {
        return Prefs.with(context).readBoolean(EXIST_IN_FIREBASE)
    }

    fun setExistingInFirebase(boolean: Boolean) {
        Prefs.with(context).writeBoolean(EXIST_IN_FIREBASE, boolean)
    }

    companion object {
        const val ID_KEY = "id key"
        const val NAME_KEY = "name key"
        const val PIC_KEY = "pic key"
        const val EXIST_IN_FIREBASE = "exist in firebase"
    }

}
