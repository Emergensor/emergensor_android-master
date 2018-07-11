package team_emergensor.co.jp.emergensor.data.service

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest
import io.reactivex.Single
import team_emergensor.co.jp.emergensor.domain.entity.EmergensorUser
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriend


class FacebookService {

    fun getMyInfo(): Single<EmergensorUser> {
        var id = ""
        var name = ""
        return Single.create<String> {
            val request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken()
            ) { _, response ->
                id = response.jsonObject.get("id").toString()
                name = response.jsonObject.get("name").toString()
                it.onSuccess(id)
            }
            val parameters = Bundle()
            parameters.putString("fields", "id,name")
            request.parameters = parameters
            request.executeAsync()
        }.flatMap {
            getPictureUrl(it)
        }.map {
            EmergensorUser(id, name, it)
        }
    }

    fun getFriends(emergensorUser: EmergensorUser): Single<Array<FacebookFriend>> {
        return Single.create<Array<FacebookFriend>> {
            val request = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/${emergensorUser.id}/friends"
            ) { res ->
                // todo: paging
                val result = arrayListOf<FacebookFriend>()
                val data = res.jsonObject.getJSONArray("data")
                val len = data.length()
                for (i in 0 until len) {
                    val firebase_id = data.getJSONObject(i).get("id").toString()
                    val name = data.getJSONObject(i).get("name").toString()
                    result.add(FacebookFriend(firebase_id, name, ""))
                }
                result.add(FacebookFriend("hoge0", "hoge0", ""))
                result.add(FacebookFriend("hoge1", "hoge1", ""))
                result.add(FacebookFriend("hoge2", "hoge2", ""))
                result.add(FacebookFriend("fuga0", "fuga0", ""))
                result.add(FacebookFriend("fuga1", "fuga1", ""))
                result.add(FacebookFriend("fuga2", "fuga2", ""))
                result.add(FacebookFriend("foo0", "foo0", ""))
                result.add(FacebookFriend("foo1", "foo1", ""))
                result.add(FacebookFriend("foo2", "foo2", ""))
                it.onSuccess(result.toTypedArray())
            }
            request.executeAsync()
        }
    }

    private fun getPictureUrl(id: String): Single<String> {
        return Single.create<String> {
            val request = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/$id/picture?redirect=0&type=large"
            ) { res ->
                val url = res.jsonObject.getJSONObject("data").get("url").toString()
                it.onSuccess(url)
            }
            request.executeAsync()
        }
    }
}