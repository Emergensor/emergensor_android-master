package team_emergensor.co.jp.emergensor.ui.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import team_emergensor.co.jp.emergensor.R
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
        return binding.root
    }
}
