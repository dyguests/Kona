package com.fanhl.kona.ui.main.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanhl.kona.R
import com.fanhl.kona.net.model.Post
import kotlinx.android.synthetic.main.item_post.view.*

class MainAdapter : BaseQuickAdapter<Post, MainAdapter.ViewHolder>(R.layout.item_post) {
    override fun convert(helper: ViewHolder?, item: Post?) {
        helper?.bind(item ?: return)
    }

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private var data: Post? = null

        fun bind(data: Post) {
            this.data = data

            Glide.with(itemView.img_cover)
                    .load("http:" + data.previewUrl)
                    .into(itemView.img_cover)
        }
    }
}