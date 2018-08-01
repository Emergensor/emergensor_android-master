package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.domain.entity.DangerousArea
import team_emergensor.co.jp.emergensor.domain.entity.DangerousType


class MapViewModel : ViewModel(), GoogleMap.OnMarkerClickListener {

    val emergencyCallPublisher = MutableLiveData<Unit>()
    val markersPublisher = MutableLiveData<Array<MarkerOptions>>()
    val circlesPublisher = MutableLiveData<Array<CircleOptions>>()
    val markerListScrollPublisher = MutableLiveData<Int>()
    val markerLookPublisher = MutableLiveData<Int>()
    val reportDialogShowPublisher = MutableLiveData<Unit>()
    val onClickBelowPublisher = MutableLiveData<Unit>()

    var canUseGPS = true // FIXME: GPS使用確認
    val adapter = MarkersAdapter()

    val markers = mutableListOf<Marker>()
    val circles = mutableListOf<Circle>()
    val markerOptions = mutableListOf<MarkerOptions>()
    val circleOptions = mutableListOf<CircleOptions>()

    var dangerousAreas = arrayOf<DangerousArea>()
        set(value) {
            field = value
            markers.forEach { it.remove() }

            markers.clear()
            markerOptions.clear()

            val markerViewModels = value.map {
                MarkerViewModel(it)
            }
            adapter.viewModels.clear()
            adapter.viewModels.addAll(markerViewModels)

            markerOptions.clear()
            field.forEach { it ->
                val marker = when (it.type) {
                    DangerousType.DISASTER ->
                        MarkerOptions()
                                .position(LatLng(it.point.latitude, it.point.longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.feed_pin))
                    DangerousType.VIOLENT ->
                        MarkerOptions()
                                .position(LatLng(it.point.latitude, it.point.longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.danger_pin))
                }
                val circle = when (it.type) {
                    DangerousType.VIOLENT ->
                        CircleOptions()
                                .center(LatLng(it.point.latitude, it.point.longitude))
                                .radius(3000.0).fillColor(Color.argb(40, 200, 0, 0))
                                .strokeWidth(0.1f)
                                .visible(true)
                    else -> null
                }
                markerOptions.add(marker)
                if (circle != null) {
                    circleOptions.add(circle)
                }
            }
            markersPublisher.postValue(markerOptions.toTypedArray())
            circlesPublisher.postValue(circleOptions.toTypedArray())
        }


    fun onFloatingActionButtonClick(view: View) {
        reportDialogShowPublisher.postValue(Unit)
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

    fun onClickBelow(view: View) {
        onClickBelowPublisher.postValue(Unit)
    }
}