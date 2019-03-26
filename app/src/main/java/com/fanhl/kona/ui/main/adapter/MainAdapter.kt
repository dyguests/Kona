package com.fanhl.kona.ui.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanhl.base.extension.setImage
import com.fanhl.kona.R
import com.fanhl.kona.domain.data.Cover
import kotlinx.android.synthetic.main.item_main_cover.view.*

class MainAdapter : BaseQuickAdapter<Cover, BaseViewHolder>(R.layout.item_main_cover) {
    override fun convert(helper: BaseViewHolder?, item: Cover?) {
        helper?.itemView?.apply {
            img_cover.setImage(
                "http://n.sinaimg.cn/sinacn13/240/w640h400/20180522/d164-hawmaua7321776.jpg",
                R.drawable.ic_launcher_foreground
            )
        }
    }
}
