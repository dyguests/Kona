package com.fanhl.kona.ui.main.adapter

import android.support.constraint.ConstraintLayout
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanhl.kona.R
import com.fanhl.kona.net.model.Post
import com.fanhl.util.SpanUtils
import com.fanhl.util.px
import kotlinx.android.synthetic.main.item_post.view.*

class MainAdapter : BaseQuickAdapter<Post, MainAdapter.ViewHolder>(R.layout.item_post) {
    override fun convert(helper: ViewHolder?, item: Post?) {
        helper?.bind(item ?: return)
    }

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private var data: Post? = null

        fun bind(data: Post) {
            this.data = data
//            itemView.img_cover.layoutParams = (itemView.img_cover.layoutParams as ConstraintLayout.LayoutParams).apply {
//                dimensionRatio = "w,${data.width ?: return}:${data.height ?: return}"
//            }
            Glide.with(itemView.img_cover)
                    .load(data.previewUrl)
//                    .load(data.previewUrl + "test")
                    .apply(RequestOptions().dontTransform().placeholder(R.drawable.ic_launcher_foreground))
                    .into(itemView.img_cover)
//            itemView.tv_tags.text = SpanUtils()
//                    .append(data.tags ?: "")
//                    .setShadow(1.px.toFloat(), 0f, 0f, ContextCompat.getColor(itemView.context, android.R.color.black))
//                    .create()
            itemView.tv_size.text = SpanUtils()
                    .append("${data.width}×${data.height}").setShadow(2.px.toFloat(), 0f, 0f, R.color.text_shadow)
                    .create()
        }
    }
}