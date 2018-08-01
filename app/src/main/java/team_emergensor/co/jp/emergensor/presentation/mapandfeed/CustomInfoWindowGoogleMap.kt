package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.domain.entity.DangerousType


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

        when (markerViewModel.type) {
            DangerousType.VIOLENT -> {
                view.setBackgroundResource(R.mipmap.marker_bg_red)
                view.findViewById<ImageView>(R.id.markerIcon).setImageResource(R.mipmap.revolver)
            }
            DangerousType.DISASTER -> {
                view.setBackgroundResource(R.mipmap.marker_window_bg_blue)
                view.findViewById<ImageView>(R.id.markerIcon).setImageResource(R.mipmap.marker_window_doc_icon)
            }
        }

        val dateTextView = view.findViewById<TextView>(R.id.date)
        val placeTextView = view.findViewById<TextView>(R.id.placeText)

        dateTextView.text = markerViewModel.dateText
        placeTextView.text = markerViewModel.description
        return view
    }

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }
}