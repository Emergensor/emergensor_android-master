package team_emergensor.co.jp.emergensor.presentation.members

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import team_emergensor.co.jp.emergensor.domain.entity.FollowingUser

class MemberViewModel(friend: FollowingUser, private val listener: FollowListener) : BaseObservable() {

    var facebookFriend: FollowingUser = friend
        set(value) {
            field = value
            name = value.name
            url = value.picture
            id = value.firebase_id
            isFollow = value.follow
        }

    @get:Bindable
    var name = friend.name
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var url = friend.picture
        set(value) {
            field = value
            notifyPropertyChanged(BR.url)
        }

    @get:Bindable
    var id = friend.firebase_id
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    var isFollow = friend.follow
        set(value) {
            field = value
            notifyPropertyChanged(BR.follow)
        }

    fun followChange(view: View) {
        isFollow = !isFollow
        listener.follow(id, isFollow)
    }

    interface FollowListener {
        fun follow(id: String, isFollow: Boolean)
    }
}