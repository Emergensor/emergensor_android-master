package team_emergensor.co.jp.emergensor.data.service

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest
import io.reactivex.Single
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriends
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo

class FacebookService {

    fun getMyInfo(): Single<MyFacebookInfo> = Single.create<MyFacebookInfo> {
        val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
        ) { _, response ->
            val id = response.jsonObject.get("id").toString()
            val name = response.jsonObject.get("name").toString()
            val info = MyFacebookInfo(id, name)
            it.onSuccess(info)
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name")
        request.parameters = parameters
        request.executeAndWait()
    }

    fun getFriends(myFacebookInfo: MyFacebookInfo): Single<Array<FacebookFriends>> =
            Single.create<Array<FacebookFriends>> {
                val request = GraphRequest.newGraphPathRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/${myFacebookInfo.id}/friends"
                ) { res ->
                    val result = arrayListOf<FacebookFriends>()
                    val data = res.jsonObject.getJSONArray("data")
                    val len = data.length()
                    for (i in 0 until len) {
                        val id = data.getJSONObject(i).get("id").toString()
                        val name = data.getJSONObject(i).get("first_name").toString()
                        result.add(FacebookFriends(id, name))
                    }
                    it.onSuccess(result.toTypedArray())
                }
                request.executeAsync()
            }
}