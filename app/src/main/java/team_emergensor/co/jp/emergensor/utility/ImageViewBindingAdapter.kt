package team_emergensor.co.jp.emergensor.utility

import android.databinding.BindingAdapter
import android.widget.ImageView
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.service.FacebookService

@BindingAdapter("userImageUrlWithId")
fun ImageView.loadImage(faceboookId: String?) {
    val id = faceboookId ?: return
    val service = FacebookService()
    service.getPictureUrl(id).subscribe { t1, t2 ->
        GlideApp.with(this.context)
                .load(t1)
                .placeholder(R.color.gray)
                .into(this)
    }
}

@BindingAdapter("imageUrl")
fun ImageView.loadImageUrl(url: String?) {
    GlideApp.with(this.context)
            .load(url)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(this)
}