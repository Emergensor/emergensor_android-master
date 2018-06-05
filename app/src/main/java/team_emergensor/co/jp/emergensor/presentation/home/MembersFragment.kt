package team_emergensor.co.jp.emergensor.presentation.home

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.repository.FacebookRepository
import team_emergensor.co.jp.emergensor.databinding.FragmentMembersBinding
import team_emergensor.co.jp.emergensor.service.acceleration.AccelerationSensorService

class MembersFragment : Fragment() {
    private lateinit var binding: FragmentMembersBinding

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_members, container, false)

        binding.toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("sensor", isChecked.toString())
            if (isChecked) {
                activity?.startService(AccelerationSensorService.createIntent(activity!!))
            } else {
                activity?.stopService(AccelerationSensorService.createIntent(activity!!))
            }
        }

        FacebookRepository(activity).getFriends().observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe { t1, t2 ->
            Log.d(TAG, t1.size.toString())
        }
        return binding.root
    }

    companion object {
        const val TAG = "MembersFragment"
    }
}
