package team_emergensor.co.jp.emergensor.presentation.settings

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModel : ViewModel() {

    val logoutPublisher = MutableLiveData<Unit>()

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        logoutPublisher.postValue(Unit)
    }
}