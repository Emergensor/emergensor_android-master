package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.RecyclerView
import android.view.View
import team_emergensor.co.jp.emergensor.domain.entity.DangerousArea

class MapViewModel : ViewModel() {
    val emergencyCallPublisher = MutableLiveData<Unit>()
    val scrollMarkerListToCurrentPublisher = MutableLiveData<Unit>()

    var canUseGPS = true // FIXME: GPS使用確認
    val adapter = MarkersAdapter()

    var dangerousAreas = arrayOf<DangerousArea>()
        set(value) {
            field = value
            val markerViewModels = value.map {
                MarkerViewModel(it)
            }
            adapter.viewModels.addAll(markerViewModels)
        }


    fun call(view: View) {
        emergencyCallPublisher.postValue(Unit)
    }

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                scrollMarkerListToCurrentPublisher.postValue(Unit)
            }
        }
    }
}