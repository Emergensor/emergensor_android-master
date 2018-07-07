package team_emergensor.co.jp.emergensor.presentation.members

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.repository.FriendsRepository
import team_emergensor.co.jp.emergensor.databinding.FragmentMembersBinding
import team_emergensor.co.jp.emergensor.presentation.home.HomeActivity
import team_emergensor.co.jp.emergensor.presentation.home.HomeViewModel

class MembersFragment : Fragment() {

    private lateinit var binding: FragmentMembersBinding

    private val friendsRepository by lazy {
        FriendsRepository(context as Activity)
    }
    private val membersViewModel by lazy {
        ViewModelProviders.of(this).get(MembersViewModel::class.java)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_members, container, false)
        binding.apply {
            viewModel = membersViewModel
            setLifecycleOwner(activity as HomeActivity)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = membersViewModel.adapter
        }
        initSubscribe()
        return binding.root
    }

    private fun initSubscribe() {
        compositeDisposable.add(
                friendsRepository.getFriends()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { friends, e ->
                            e?.let {
                                Log.e(TAG, it.message)
                                return@let
                            }
                            membersViewModel.setMember(friends)
                        }
        )
        val observer = Observer<Unit> {
            ViewModelProviders.of(activity as HomeActivity).get(HomeViewModel::class.java).backHome()
        }
        membersViewModel.backPublisher.observe(activity as HomeActivity, Observer {
            ViewModelProviders.of(activity as HomeActivity).get(HomeViewModel::class.java).backHome()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        const val TAG = "Members Fragment"
    }
}
