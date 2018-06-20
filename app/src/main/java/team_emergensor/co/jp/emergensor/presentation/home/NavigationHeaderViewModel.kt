package team_emergensor.co.jp.emergensor.presentation.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo

class NavigationHeaderViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val url = MutableLiveData<String>()

    fun setMyInfo(myFacebookInfo: MyFacebookInfo) {
        name.value = myFacebookInfo.name
        url.value = myFacebookInfo.pictureUrl
    }
}