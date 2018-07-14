package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.domain.entity.DangerousArea


class MapViewModel : ViewModel(), GoogleMap.OnMarkerClickListener {

    val emergencyCallPublisher = MutableLiveData<Unit>()
    val markersPublisher = MutableLiveData<Array<MarkerOptions>>()
    val markerListScrollPublisher = MutableLiveData<Int>()
    val markerLookPublisher = MutableLiveData<Int>()

    var canUseGPS = true // FIXME: GPS使用確認
    val adapter = MarkersAdapter()

    val markers = mutableListOf<Marker>()
    val markerOptions = mutableListOf<MarkerOptions>()

    var dangerousAreas = arrayOf<DangerousArea>()
        set(value) {
            field = value
            val markerViewModels = value.map {
                MarkerViewModel(it)
            }
            adapter.viewModels.addAll(markerViewModels)

            markerOptions.clear()
            field.forEach { it ->
                val marker = MarkerOptions()
                        .position(LatLng(it.point.latitude, it.point.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.feed_pin))
                markerOptions.add(marker)
            }
            markersPublisher.postValue(markerOptions.toTypedArray())
        }


    fun call(view: View) {
        emergencyCallPublisher.postValue(Unit)
    }


    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (recyclerView == null) return
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    val nextId = manager.findFirstCompletelyVisibleItemPosition()
                    if (nextId >= 0 && nextId < markers.size) {
                        manager.smoothScrollToPosition(recyclerView, RecyclerView.State(), nextId)
                        markers[nextId].showInfoWindow()
                        markerLookPublisher.postValue(nextId)
                    }
                }
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker == null) return false
        val id = markers.indexOf(marker)
        if (marker.isInfoWindowShown) return false
        if (id == -1) return false
        markerListScrollPublisher.postValue(id)
        return true
    }
}