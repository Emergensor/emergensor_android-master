package team_emergensor.co.jp.emergensor.presentation.members

import android.databinding.BaseObservable
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriend

class MemberViewModel(friend: FacebookFriend) : BaseObservable() {

    var facebookFriend: FacebookFriend = friend
        set(value) {
            field = value
            name = value.name
            url = value.url
            id = value.id
        }

    var name = friend.name
    var url = friend.url
    var id = friend.id
    var isFollow = false
}