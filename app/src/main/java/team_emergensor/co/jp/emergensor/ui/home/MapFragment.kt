package team_emergensor.co.jp.emergensor.ui.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.AccessToken
import com.facebook.GraphRequest
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.FragmentMapBinding


class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            val id = response.jsonObject.get("id")
            val request = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/$id/friends"
            ) {
                Log.d("response", it.rawResponse)
            }

            request.executeAsync()
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link")
        request.parameters = parameters
        request.executeAsync()

        return binding.root
    }

}

