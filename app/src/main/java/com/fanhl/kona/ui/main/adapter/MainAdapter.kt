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
                "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=179474400,2264175345&fm=58&bpow=1440&bpoh=900",
                R.drawable.ic_launcher_foreground
            )
        }
    }
}
