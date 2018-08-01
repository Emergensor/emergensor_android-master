package team_emergensor.co.jp.emergensor.presentation.analysys

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class AnalysysViewModel : ViewModel() {
    var isSensorLogging = false
        set(value) {
            field = value
            if (value) {
                description.postValue("current action: ")
            } else {
                description.postValue("turn on acceleration analysys")
            }
        }

    val actionName = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    init {
        isSensorLogging = false
    }
}
