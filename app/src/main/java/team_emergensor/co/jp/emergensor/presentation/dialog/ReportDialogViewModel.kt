package team_emergensor.co.jp.emergensor.presentation.dialog

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import team_emergensor.co.jp.emergensor.domain.entity.DangerousType


class ReportDialogViewModel : ViewModel() {

    val reportPublisher = PublishSubject.create<ReportDialogFragment.Companion.Report>()!!

    fun publish(report: ReportDialogFragment.Companion.Report) {
        reportPublisher.onNext(report)
    }

    var description: String = ""
    var type: DangerousType = DangerousType.VIOLENT
}