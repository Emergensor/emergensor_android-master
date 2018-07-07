package team_emergensor.co.jp.emergensor.presentation.members

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriend

class MembersViewModel : ViewModel() {

    val adapter = MembersAdapter(arrayOf())

    val backPublisher = MutableLiveData<Unit>()
    val followPublisher = MutableLiveData<String>()
    val unfollowPublisher = MutableLiveData<String>()

    val uid = MutableLiveData<String>()

    fun setMember(members: Array<FacebookFriend>) {
        adapter.memberViewModels.clear()
        val vms = members.map {
            MemberViewModel(it)
        }
        adapter.memberViewModels.addAll(vms)
    }

    fun addMember(members: Array<FacebookFriend>) {
        val vms = members.map {
            MemberViewModel(it)
        }
        adapter.memberViewModels.addAll(vms)
    }

    fun back(view: View) {
        backPublisher.postValue(Unit)
    }

    fun follow(id: String) {
        followPublisher.postValue(id)
    }

    fun unfollow(id: String) {
        unfollowPublisher.postValue(id)
    }
}