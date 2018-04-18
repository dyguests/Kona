package com.fanhl.kona.ui.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.fanhl.kona.R
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.ui.main.adapter.MainAdapter
import com.fanhl.kona.util.subscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {
    private val adapter by lazy {
        MainAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                (adapter as MainAdapter).data[position]
                toast("tap cover $")
            }
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                loadData(true)
            }, recycler_view)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        assignViews()

        refreshData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun assignViews() {
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        swipe_refresh_layout.setOnRefreshListener { refreshData() }
        recycler_view.adapter = adapter
    }

    private fun refreshData() {
        swipe_refresh_layout.apply { post { isRefreshing = true } }
        loadData()
    }

    private fun loadData(loadMore: Boolean = false) {
        app.client.postService
                .getPost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (loadMore) {
                                adapter.addData(it)
                            } else {
                                adapter.setNewData(it)
                            }
                            adapter.loadMoreEnd()
                        },
                        onError = {
                            swipe_refresh_layout.apply { post { isRefreshing = false } }
                            adapter.loadMoreFail()
                        },
                        onComplete = {
                            swipe_refresh_layout.apply { post { isRefreshing = false } }
                            adapter.loadMoreComplete()
                        }
                )
    }

    companion object {
        /** TAG */
        private val TAG = MainActivity::class.java.simpleName!!
    }
}
