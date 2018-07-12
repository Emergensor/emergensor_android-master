package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.ViewItemMarkerListBinding
import team_emergensor.co.jp.emergensor.domain.entity.FacebookFriend

class MarkersAdapter(markerViewModels: Array<MarkerViewModel>): RecyclerView.Adapter<MarkersAdapter.ItemViewHolder>() {

    constructor(): this(arrayOf())

    val viewModels = ObservableArrayList<MarkerViewModel>()

    init {
        viewModels.addAll(markerViewModels)
        viewModels.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<FacebookFriend>>() {
            override fun onChanged(sender: ObservableArrayList<FacebookFriend>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableArrayList<FacebookFriend>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableArrayList<FacebookFriend>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                for (i in 0..itemCount) {
                    notifyItemMoved(fromPosition + i, toPosition + i)
                }
            }

            override fun onItemRangeInserted(sender: ObservableArrayList<FacebookFriend>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeChanged(sender: ObservableArrayList<FacebookFriend>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.view_item_marker_list, parent, false)
        return ItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        if (holder == null) return

        val vm = viewModels[position]

        holder.binding?.apply {
            viewModel = vm
//            executePendingBindings()
        }
    }

    // ViewHolder
    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding = DataBindingUtil.bind<ViewItemMarkerListBinding>(v)
    }
}