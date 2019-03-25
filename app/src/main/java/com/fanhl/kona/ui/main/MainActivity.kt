package com.fanhl.kona.ui.main

import android.os.Bundle
import com.fanhl.kona.R
import com.fanhl.kona.base.ui.BaseActivity
import com.fanhl.kona.domain.data.Cover
import com.fanhl.kona.ui.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private val adapter by lazy { MainAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        refreshData()
    }

    private fun initData() {
        recycler_view.adapter = adapter
    }

    private fun refreshData() {
        adapter.setNewData(
            List(20) { Cover() }
        )
    }
}
