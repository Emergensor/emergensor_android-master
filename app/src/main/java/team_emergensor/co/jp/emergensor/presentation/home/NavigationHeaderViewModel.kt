package team_emergensor.co.jp.emergensor.presentation.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import team_emergensor.co.jp.emergensor.domain.entity.EmergensorUser

class NavigationHeaderViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val url = MutableLiveData<String>()

    var facebookInfo: EmergensorUser? = null
        set(value) {
            if (value == null) return
            field = value
            name.postValue(value.name)
            url.postValue(value.pictureUrl)
        }
}