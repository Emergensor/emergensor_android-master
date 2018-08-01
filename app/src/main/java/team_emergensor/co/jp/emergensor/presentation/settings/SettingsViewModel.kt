package team_emergensor.co.jp.emergensor.presentation.settings

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Build
import android.view.View
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.prefs.Prefs
import team_emergensor.co.jp.emergensor.service.acceleration.AccelerationSensorService

class SettingsViewModel : ViewModel() {

    val logoutPublisher = MutableLiveData<Unit>()

    val sensorLoading = MutableLiveData<Boolean>()

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        logoutPublisher.postValue(Unit)
    }

    fun settingAcceleration(view: View) {
        val intent = AccelerationSensorService.createIntent(view.context)
        val viewBool = if (sensorLoading.value == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                view.context.startForegroundService(intent)
            } else {
                view.context.startService(intent)
            }
            true
        } else {
            view.context.stopService(intent)
            false
        }
        if (sensorLoading.value != null) {
            sensorLoading.postValue(viewBool)
            Prefs.with(view.context).writeBoolean(SettingsFragment.TAG, viewBool)
        }
    }
}