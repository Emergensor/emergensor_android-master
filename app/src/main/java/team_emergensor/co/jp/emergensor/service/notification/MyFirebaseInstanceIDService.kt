package team_emergensor.co.jp.emergensor.service.notification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import team_emergensor.co.jp.emergensor.data.repository.EmergensorUserRepository


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    private val emergensorUserRepository by lazy {
        EmergensorUserRepository(applicationContext)
    }

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token ?: return
        // 自前サーバーへのRegistrationId送信処理を実装
        Log.i("firebase messaginng", "Refreshed token: " + refreshedToken)
        emergensorUserRepository.sendTokenId(refreshedToken)
    }


}