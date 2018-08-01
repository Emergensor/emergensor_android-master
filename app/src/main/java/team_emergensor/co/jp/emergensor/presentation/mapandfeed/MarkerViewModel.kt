package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import team_emergensor.co.jp.emergensor.domain.entity.DangerousArea
import team_emergensor.co.jp.emergensor.utility.DateUtil
import java.util.*

class MarkerViewModel(dangerousArea: DangerousArea) : BaseObservable() {

    @get:Bindable
    var description = dangerousArea.description
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    @get:Bindable
    var id = dangerousArea.facebookId
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    var pictureUrl = dangerousArea.picture
        set(value) {
            field = value
            notifyPropertyChanged(BR.pictureUrl)
        }

    var point = dangerousArea.point
        set(value) {
            field = value
            pointName = "lat: ${field.latitude}, lon: ${field.longitude}"
        }

    @get:Bindable
    var pointName = "lat: ${dangerousArea.point.latitude}, lon: ${dangerousArea.point.longitude}"
        set(value) {
            field = value
            notifyPropertyChanged(BR.pointName)
        }

    @get:Bindable
    var date = dangerousArea.date
        set(value) {
            field = value
            notifyPropertyChanged(BR.date)
        }

    @get:Bindable
    var dateText = DateUtil.getDiffString(dangerousArea.date, Calendar.getInstance().time)

    val type = dangerousArea.type
}