package team_emergensor.co.jp.emergensor.presentation.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.FragmentSettingsBinding
import team_emergensor.co.jp.emergensor.presentation.home.HomeActivity
import team_emergensor.co.jp.emergensor.presentation.login.LoginActivity

class SettingsFragment : Fragment() {

    private val activity by lazy { getActivity() as HomeActivity }

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by lazy {
        ViewModelProviders.of(activity).get(SettingsViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.apply {
            viewModel = this@SettingsFragment.viewModel
            setLifecycleOwner(activity)
        }
        initSubscribe()
        return binding.root
    }

    private fun initSubscribe() {
        viewModel.logoutPublisher.observe(activity, Observer<Unit> {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra(LoginActivity.IS_AUTO_START, false)
            startActivity(intent)
            activity.finish()
        })
    }
}
