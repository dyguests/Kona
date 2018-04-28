package com.fanhl.kona.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import com.fanhl.kona.R
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.ui.gallery.GalleryActivity
import com.fanhl.kona.ui.main.adapter.MainAdapter
import com.fanhl.kona.util.SystemUtils
import com.fanhl.kona.util.subscribeBy
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {
    private val adapter by lazy {
        MainAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val post = (adapter as MainAdapter).data[position]
                GalleryActivity.launch(this@MainActivity, post)
            }
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                loadData(true)
            }, recycler_view)
        }
    }

    private var page: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        assignViews()

        refreshData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun assignViews() {
        RxTextView.editorActionEvents(tv_tags)
                .throttleFirst(1, TimeUnit.SECONDS)
                .filter { it.actionId() == EditorInfo.IME_ACTION_SEARCH }
                .subscribe {
                    tv_tags.clearFocus()
                    refreshData()
                    SystemUtils.hideSoftInput(tv_tags)
                }

        tv_tags.setText("landscape")//fixme test

        swipe_refresh_layout.setOnRefreshListener { refreshData() }
        swipe_refresh_layout.setColorSchemeResources(R.color.accent)
        recycler_view.adapter = adapter
//        recycler_view.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f)).apply {
//            addDuration = 1000
//            changeDuration = 1000
//            moveDuration = 1000
//            removeDuration = 1000
//        }
    }

    private fun refreshData() {
        swipe_refresh_layout.apply { post { isRefreshing = true } }
        page = 1
        loadData()
    }

    private fun loadData(loadMore: Boolean = false) {
        app.client.postService
                .getPost(
                        tags = tv_tags.text.toString(),
                        page = page
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (loadMore) {
                                adapter.addData(it)
                            } else {
                                adapter.setNewData(it)
                            }
                            adapter.loadMoreComplete()

                            page++
//                            adapter.loadMoreEnd()
                        },
                        onError = {
                            swipe_refresh_layout.apply { post { isRefreshing = false } }
                            adapter.loadMoreFail()
                        },
                        onComplete = {
                            swipe_refresh_layout.apply { post { isRefreshing = false } }
                        }
                )
    }

    companion object {
        /** TAG */
        private val TAG = MainActivity::class.java.simpleName!!
    }
}
