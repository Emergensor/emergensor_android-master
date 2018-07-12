package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import team_emergensor.co.jp.emergensor.domain.entity.DangerousArea

class MapViewModel : ViewModel() {
    val emergencyCallPublisher = MutableLiveData<Unit>()
    val scrollMarkerListToCurrentPublisher = MutableLiveData<Unit>()

    val markersPublisher = MutableLiveData<Array<MarkerOptions>>()

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
}