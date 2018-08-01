package team_emergensor.co.jp.emergensor.data.service

import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import io.reactivex.Single
import org.json.JSONObject
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
                val result = arrayListOf<FacebookFriend>()
                try {


                    val data = res.jsonObject.getJSONArray("data")
                    val len = data.length()
                    val jsonList = mutableListOf<JSONObject>()
                    (0 until len).mapTo(jsonList) { data.getJSONObject(it) }
                    Single.zipArray(
                            { array ->
                                array.forEach {
                                    if (it is FacebookFriend) result.add(it)
                                }
                                it.onSuccess(result.toTypedArray())
                            },
                            jsonList.map {
                                Single.create<FacebookFriend> { res ->
                                    val id = it.get("id").toString()
                                    val name = it.get("name").toString()
                                    getPictureUrl(id).subscribe { t1, t2 ->
                                        if (t2 != null) {
                                            res.onError(t2)
                                            return@subscribe
                                        }
                                        res.onSuccess(FacebookFriend(id, name, t1))
                                    }
                                }
                            }.toTypedArray()
                    ).subscribe()
                } catch (e: Exception) {
                    Log.e("facebook", e.message)
                }
            }
            request.executeAsync()
        }
    }

    fun getPictureUrl(id: String): Single<String> {
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