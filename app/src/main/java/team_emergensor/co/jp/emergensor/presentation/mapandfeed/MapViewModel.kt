package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View

class MapViewModel : ViewModel() {
    val emergencyCallPublisher = MutableLiveData<Unit>()

    var canUseGPS = true // FIXME: GPS使用確認

    fun call(view: View) {
        emergencyCallPublisher.postValue(Unit)
    }
}