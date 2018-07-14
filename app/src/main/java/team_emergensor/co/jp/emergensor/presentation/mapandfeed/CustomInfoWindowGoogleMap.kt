package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import team_emergensor.co.jp.emergensor.R


class CustomInfoWindowGoogleMap(private val mapFragment: MapFragment) : GoogleMap.InfoWindowAdapter {

    private val viewModel by lazy {
        ViewModelProviders.of(mapFragment).get(MapViewModel::class.java)
    }


    override fun getInfoWindow(p0: Marker?): View? {
        val view = (mapFragment).layoutInflater.inflate(R.layout.view_item_marker, null)
        val marker = p0 ?: return view
        val id = viewModel.markers.indexOf(marker)
        if (id == -1) return null
        val markerViewModel = viewModel.adapter.viewModels[id]

        val dateTextView = view.findViewById<TextView>(R.id.date)
        val placeTextView = view.findViewById<TextView>(R.id.placeText)

        dateTextView.text = markerViewModel.date.toString()
        placeTextView.text = markerViewModel.pointName
        return view
    }

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }
}