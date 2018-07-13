package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import team_emergensor.co.jp.emergensor.domain.entity.DangerousArea

class MapViewModel : ViewModel(), GoogleMap.OnMarkerClickListener {
    val emergencyCallPublisher = MutableLiveData<Unit>()

    val markersPublisher = MutableLiveData<Array<MarkerOptions>>()
    val markerListScrollPublisher = MutableLiveData<Int>()
    val markerLookPublisher = MutableLiveData<Int>()

    var canUseGPS = true // FIXME: GPS使用確認
    val adapter = MarkersAdapter()

    var dangerousAreas = arrayOf<DangerousArea>()
        set(value) {
            field = value
            val markerViewModels = value.map {
                MarkerViewModel(it)
            }
            adapter.viewModels.addAll(markerViewModels)

            val markers = mutableListOf<MarkerOptions>()
            field.forEach {
                val marker = MarkerOptions().position(LatLng(it.point.latitude, it.point.longitude)).title(it.description)
                markers.add(marker)
            }
            markersPublisher.postValue(markers.toTypedArray())
        }


    fun call(view: View) {
        emergencyCallPublisher.postValue(Unit)
    }

    val markers = mutableListOf<Pair<Int, Marker>>()

    val scrollListener = object : RecyclerView.OnScrollListener() {
        private var isScrollRight = true
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    val visibleItemCount = recyclerView?.childCount ?: return
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItem = manager.findFirstVisibleItemPosition()
                    val lastInScreen = firstVisibleItem + visibleItemCount - 1
                    if (isScrollRight) {
                        manager.smoothScrollToPosition(recyclerView, RecyclerView.State(), lastInScreen)
                    } else {
                        manager.smoothScrollToPosition(recyclerView, RecyclerView.State(), firstVisibleItem)
                    }
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isScrollRight = dx > 0
            val visibleItemCount = recyclerView?.childCount ?: return
            val manager = recyclerView?.layoutManager as LinearLayoutManager
            val firstVisibleItem = manager.findFirstVisibleItemPosition()
            val lastInScreen = firstVisibleItem + visibleItemCount - 1
            val nextItemId = if (isScrollRight) {
                firstVisibleItem
            } else {
                lastInScreen
            }
            markerLookPublisher.postValue(nextItemId)
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val pair = markers.firstOrNull { it.second == marker } ?: return true
        markerListScrollPublisher.postValue(pair.first)
        return true
    }
}