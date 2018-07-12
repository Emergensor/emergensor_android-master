package team_emergensor.co.jp.emergensor.utility

import android.databinding.BindingAdapter
import android.widget.ImageView
import team_emergensor.co.jp.emergensor.R

@BindingAdapter("userImageUrl")
fun ImageView.loadImage(url: String?) {
    GlideApp.with(this.context)
            .load(url)
            .placeholder(R.color.gray)
            .into(this)

}

@BindingAdapter("imageUrl")
fun ImageView.loadImageUrl(url: String?) {
    GlideApp.with(this.context)
            .load(url)
            .placeholder(R.color.gray)
            .into(this)
}