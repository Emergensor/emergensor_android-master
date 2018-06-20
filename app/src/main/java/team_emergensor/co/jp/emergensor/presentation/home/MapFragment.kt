package team_emergensor.co.jp.emergensor.presentation.home

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.FragmentMapBinding


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

//        binding.logout.text = "logout"
//        binding.logout.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            LoginManager.getInstance().logOut()
//            val intent = Intent(activity, LoginActivity::class.java)
//            intent.putExtra(LoginActivity.IS_AUTO_START, false)
//            startActivity(intent)
//        }

        return binding.root
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

}

