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
                "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2402068196,3438361974&fm=85&app=52&f=PNG?w=121&h=75&s=119E3C72AED07FE14D75D4C4030070B3",
                R.drawable.ic_launcher_foreground
            )
        }
    }
}
