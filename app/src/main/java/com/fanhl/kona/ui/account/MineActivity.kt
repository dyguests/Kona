package com.fanhl.kona.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.view.View
import com.fanhl.kona.R
import com.fanhl.kona.model.Post
import com.fanhl.kona.ui.account.adapter.PostHorizontalAdapter
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.ui.gallery.GalleryActivity
import com.fanhl.kona.util.app
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mine.*
import kotlinx.android.synthetic.main.partial_mine_item.view.*
import org.jetbrains.anko.doAsync


class MineActivity : BaseActivity() {
    private val favoriteViewHolder by lazy {
        FavoriteViewHolder(favorite) {
            GalleryActivity.launchForResult(this@MineActivity, REQUEST_CODE_MINE, it)
        }
    }
    private val historyViewHolder by lazy {
        HistoryViewHolder(history) {
            GalleryActivity.launchForResult(this@MineActivity, REQUEST_CODE_MINE, it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        assignViews()
        initData()
        refreshData()
    }

    private fun assignViews() {
        app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = true
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar_layout.title = getString(R.string.title_activity_mine)
                    isShow = true
                } else if (isShow) {
                    toolbar_layout.title = " "//carefull there should a space between double quote otherwise it wont work
                    isShow = false
                }
            }
        })

        favoriteViewHolder.assignViews()
        historyViewHolder.assignViews()
    }

    private fun initData() {
        favoriteViewHolder.initData()
        historyViewHolder.initData()
    }

    private fun refreshData() {
        favoriteViewHolder.refreshData()
        historyViewHolder.refreshData()
    }

    companion object {
        private const val REQUEST_CODE_MINE = 20002

        fun launch(context: Context, bundle: Bundle? = null) {
            context.startActivity(Intent(context, MineActivity::class.java), bundle)
        }
    }

    class FavoriteViewHolder(private val root: View, onItemClick: (Post) -> Unit) {
        private val adapter by lazy {
            PostHorizontalAdapter().apply {
                setOnItemClickListener { adapter, view, position ->
                    val post = (adapter as PostHorizontalAdapter).data.getOrNull(position)
                            ?: return@setOnItemClickListener
                    onItemClick(post)
                }
                setOnItemLongClickListener { adapter, view, position ->
                    val post = (adapter as PostHorizontalAdapter).data.getOrNull(position)
                            ?: return@setOnItemLongClickListener true

                    Snackbar.make(root.recycler_view, R.string.confirm_delete_post, Snackbar.LENGTH_SHORT).setAction(R.string.delete) {
                        doAsync {
                            root.app.db.postDao().delete(post)
                        }
                        adapter.remove(position)
                    }.show()
                    return@setOnItemLongClickListener true
                }
            }
        }

        fun assignViews() {
//            LinearSnapHelper().attachToRecyclerView(root.recycler_view)
        }

        fun initData() {
            root.apply {
                tv_title.setText(R.string.favorite)
                recycler_view.adapter = adapter
            }
        }

        fun refreshData() {
            root.app.db.postDao().getLastFavorites(1000)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = {
                                adapter.setNewData(it)
                            },
                            onError = {}
                    )
        }
    }

    class HistoryViewHolder(private val root: View, onItemClick: (Post) -> Unit) {
        private val adapter by lazy {
            PostHorizontalAdapter().apply {
                setOnItemClickListener { adapter, view, position ->
                    val post = (adapter as PostHorizontalAdapter).data.getOrNull(position)
                            ?: return@setOnItemClickListener
                    onItemClick(post)
                }
                setOnItemLongClickListener { adapter, view, position ->
                    val post = (adapter as PostHorizontalAdapter).data.getOrNull(position)
                            ?: return@setOnItemLongClickListener true

                    Snackbar.make(root.recycler_view, R.string.confirm_delete_post, Snackbar.LENGTH_SHORT).setAction(R.string.delete) {
                        doAsync {
                            root.app.db.postDao().delete(post)
                        }
                        adapter.remove(position)
                    }.show()
                    return@setOnItemLongClickListener true
                }
            }
        }

        fun assignViews() {
//            LinearSnapHelper().attachToRecyclerView(root.recycler_view)
        }

        fun initData() {
            root.apply {
                tv_title.setText(R.string.history)
                recycler_view.adapter = adapter
            }
        }

        fun refreshData() {
            root.app.db.postDao().getLast(1000)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = {
                                adapter.setNewData(it)
                            },
                            onError = {}
                    )
        }
    }
}
