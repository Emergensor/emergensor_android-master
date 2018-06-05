package team_emergensor.co.jp.emergensor.presentation.home

import android.app.Fragment
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.FragmentMapBinding
import team_emergensor.co.jp.emergensor.presentation.login.LoginActivity


class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

}

