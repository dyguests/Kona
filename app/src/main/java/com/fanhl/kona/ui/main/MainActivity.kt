package com.fanhl.kona.ui.main

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.fanhl.kona.R
import com.fanhl.kona.model.Tag
import com.fanhl.kona.net.model.Post
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.ui.gallery.GalleryActivity
import com.fanhl.kona.ui.main.adapter.MainAdapter
import com.fanhl.kona.util.SystemUtils
import com.fanhl.kona.util.observe
import com.jaeger.library.StatusBarUtil
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity() {
    /**tags历史记录列表*/
    private val tagAdapter by lazy {
        ArrayAdapter<String>(this, R.layout.item_tag_dropdown)
    }

    /**cover列表*/
    private val adapter by lazy {
        MainAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val post = (adapter as MainAdapter).data.getOrNull(position)
                        ?: return@setOnItemClickListener
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_account_circle_white_24px)
        prepareData()
        assignViews()
        initData()
        refreshData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            true
        }
        R.id.action_clear -> {
            tv_tags.setText("")
            actionSearch()
            true
        }
        R.id.action_search -> {
            actionSearch()
            true
        }
        R.id.action_settings -> true
        else -> super.onOptionsItemSelected(item)
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
//        RxTextView.textChanges(tv_tags)
//                .toFlowable(BackpressureStrategy.LATEST)
//                .flatMap {
//                    app.db.tagDao().getAllLikeName(it.toString())
//                }
//                .map { it.map { it.name } }
//                .subscribe {
//                    tagAdapter.clear()
//                    tagAdapter.addAll(it)
//                }
        RxTextView.editorActionEvents(tv_tags)
                .throttleFirst(1, TimeUnit.SECONDS)
                .filter { it.actionId() == EditorInfo.IME_ACTION_SEARCH }
                .subscribe {
                    actionSearch()
                }
        RxAutoCompleteTextView.itemClickEvents(tv_tags)
                .subscribe { actionSearch() }
//        tv_tags.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
////                parent?.adapter?.getItem(position) as String
//                actionSearch()
//            }
//
//        }
        tv_tags.setAdapter(tagAdapter)

        swipe_refresh_layout.setOnRefreshListener { refreshData() }
        swipe_refresh_layout.setColorSchemeResources(R.color.accent)
        recycler_view.adapter = adapter
//        recycler_view.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f)).apply {
//            addDuration = 1000
//            changeDuration = 1000
//            moveDuration = 1000
//            removeDuration = 1000
//        }

        viewModel.tag.observe(this) { tv_tags.setText(it) }
// BRVAH not support DiffUtil.OTL
//        viewModel.posts.observe(this) {
//            DiffUtil
//                    .calculateDiff(object : DiffUtil.Callback() {
//                        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = adapter.data[oldItemPosition].id == it?.get(newItemPosition)?.id ?: -1
//
//                        override fun getOldListSize() = adapter.data.size
//
//                        override fun getNewListSize() = it?.size ?: 0
//
//                        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//                            val oldPost = adapter.data[oldItemPosition]
//                            val newPost = it?.get(newItemPosition)
//                            return newPost != null
//                                    && oldPost.previewUrl == newPost.previewUrl
//                                    && oldPost.width == newPost.width
//                                    && oldPost.height == newPost.height
//                        }
//                    })
////                    .dispatchUpdatesTo(adapter)
//                    .dispatchUpdatesTo(object : ListUpdateCallback {
//                        override fun onChanged(position: Int, count: Int, payload: Any?) {
//                        }
//
//                        override fun onMoved(fromPosition: Int, toPosition: Int) {
//                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                        }
//
//                        override fun onInserted(position: Int, count: Int) {
//                            adapter.add
//                        }
//
//                        override fun onRemoved(position: Int, count: Int) {
//                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                        }
//                    })
////            adapter.setNewData(it)
//            adapter.replaceData()
//        }
    }

    private fun initData() {
        app.db.tagDao().getLast()
                .subscribeOn(Schedulers.io())
                .map { it.name }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { viewModel.tag.value = it },
                        onError = { viewModel.tag.value = "landscape" }
                )

        app.db.tagDao().getLast(1000)
                .subscribeOn(Schedulers.io())
                .map { it.map { it.name } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            tagAdapter.clear()
                            tagAdapter.addAll(it)
                        }
                )

        adapter.setNewData(viewModel.posts.value)
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
                                viewModel.posts.value = viewModel.posts.value.orEmpty().toMutableList().apply { addAll(it) }
                            } else {
                                adapter.setNewData(it)
                                viewModel.posts.value = it
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

//        Flowable
//                .fromCallable {
//                    app.db.tagDao().insertAll(Tag(name = tv_tags.text.toString()))
//                }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()

        doAsync {
            app.db.tagDao().insertAll(Tag(name = tv_tags.text.toString()))
        }
    }

    companion object {
        /** TAG */
        private val TAG = MainActivity::class.java.simpleName!!

        private const val REQUEST_CODE_MAIN = 20001
    }

    class ViewModel : android.arch.lifecycle.ViewModel() {
        val tag by lazy { MutableLiveData<String>() }
        val page by lazy { MutableLiveData<Int>() }
        val posts by lazy { MutableLiveData<List<Post>>() }
    }
}
