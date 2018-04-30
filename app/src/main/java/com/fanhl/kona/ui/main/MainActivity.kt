package com.fanhl.kona.ui.main

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import com.fanhl.kona.R
import com.fanhl.kona.model.Tag
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.ui.gallery.GalleryActivity
import com.fanhl.kona.ui.main.adapter.MainAdapter
import com.fanhl.kona.util.SystemUtils
import com.jaeger.library.StatusBarUtil
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity() {
    /**tags历史记录列表*/
    private val tagAdapter by lazy { ArrayAdapter<String>(this, R.layout.item_tag_dropdown) }

    /**cover列表*/
    private val adapter by lazy {
        MainAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val post = (adapter as MainAdapter).data[position]
                GalleryActivity.launchForResult(this@MainActivity, REQUEST_CODE_MAIN, post)
            }
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                loadData(true)
            }, recycler_view)
        }
    }

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparent(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        prepareData()
        assignViews()
        initData()
        refreshData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                tv_tags.setText("")
                true
            }
            R.id.action_search -> {
                actionSearch()
                true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_MAIN -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val tag = data?.getStringExtra(GalleryActivity.RESULT_DATA_TAG)
                        tv_tags.setText(tag)
                        refreshData()

                        doAsync {
                            app.db.tagDao().insertAll(Tag(name = tv_tags.text.toString()))
                        }
                    }
                    else -> {
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun prepareData() {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModel.page.value = 1
    }

    private fun assignViews() {
        tv_tags.setAdapter(tagAdapter)
        RxTextView.editorActionEvents(tv_tags)
                .throttleFirst(1, TimeUnit.SECONDS)
                .filter { it.actionId() == EditorInfo.IME_ACTION_SEARCH }
                .subscribe {
                    actionSearch()
                }

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

    private fun initData() {
        //fixme test
        tv_tags.setText("landscape")

        app.db.tagDao().getAll()
                .subscribeOn(Schedulers.io())
                .map { it.map { it.name } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            tagAdapter.clear()
                            tagAdapter.addAll(it)
                        }
                )
        doAsync {
            app.db.tagDao().getAll()
            tagAdapter.addAll()
        }
    }

    private fun refreshData() {
        swipe_refresh_layout.apply { post { isRefreshing = true } }
        viewModel.page.value = 1
        loadData()
    }

    private fun loadData(loadMore: Boolean = false) {
        app.client.postService
                .getPost(
                        tags = tv_tags.text.toString(),
                        page = viewModel.page.value
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

                            if (it.isNotEmpty()) {
                                viewModel.page.value = viewModel.page.value!! + 1
                            } else {
                                adapter.loadMoreEnd()
                            }
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

    private fun actionSearch() {
        tv_tags.clearFocus()
        refreshData()
        SystemUtils.hideSoftInput(tv_tags)

        Flowable
                .fromCallable {
                    app.db.tagDao().insertAll(Tag(name = tv_tags.text.toString()))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    companion object {
        /** TAG */
        private val TAG = MainActivity::class.java.simpleName!!

        private const val REQUEST_CODE_MAIN = 20001
    }

    class ViewModel : android.arch.lifecycle.ViewModel() {
        val page by lazy { MutableLiveData<Int>() }
    }
}
