package team_emergensor.co.jp.emergensor.presentation.members

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.repository.FriendsRepository
import team_emergensor.co.jp.emergensor.databinding.FragmentMembersBinding

class MembersFragment : Fragment() {

    private lateinit var binding: FragmentMembersBinding

    private val friendsRepository by lazy {
        FriendsRepository(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_members, container, false)
        initFollowing()
        initFollower()
        initNewFollow()
        return binding.root
    }

    private fun initFollowing() {

    }

    private fun initFollower() {

    }

    private fun initNewFollow() {
        friendsRepository.getFriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it, e ->
                    e?.let {
                        return@subscribe
                    }
                }
    }
}
