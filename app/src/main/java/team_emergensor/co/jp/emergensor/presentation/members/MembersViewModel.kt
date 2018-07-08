package team_emergensor.co.jp.emergensor.presentation.members

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import team_emergensor.co.jp.emergensor.domain.entity.FollowingUser

class MembersViewModel : ViewModel(), MemberViewModel.FollowListener {
    val adapter = MembersAdapter(arrayOf())

    val followPublisher = MutableLiveData<Pair<Boolean, String>>()

    fun setMember(members: Array<FollowingUser>) {
        adapter.memberViewModels.clear()
        val vms = members.map {
            MemberViewModel(it, this)
        }
        adapter.memberViewModels.addAll(vms)
    }

    fun addMember(members: Array<FollowingUser>) {
        val vms = members.map {
            MemberViewModel(it, this)
        }
        adapter.memberViewModels.addAll(vms)
    }

    override fun follow(id: String, isFollow: Boolean) {
        followPublisher.postValue(Pair(isFollow, id))
    }
}