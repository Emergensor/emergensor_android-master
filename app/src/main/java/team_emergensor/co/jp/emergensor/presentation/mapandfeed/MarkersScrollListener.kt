package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class MarkersScrollListener(private val manager: LinearLayoutManager): RecyclerView.OnScrollListener() {
    var firstVisibleItem: Int = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var previousTotal = 0
    private var loading = true
    private var current_page = 1

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView!!.childCount
        totalItemCount = manager.itemCount
        firstVisibleItem = manager.findFirstCompletelyVisibleItemPosition()

        manager.scrollToPosition(firstVisibleItem)
    }

}