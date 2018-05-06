package com.fanhl.kona.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.SnapHelper
import android.view.View
import com.fanhl.kona.R
import com.fanhl.kona.model.Post
import com.fanhl.kona.ui.account.adapter.HistoryAdapter
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.ui.gallery.GalleryActivity
import com.fanhl.kona.ui.main.adapter.MainAdapter
import com.fanhl.kona.util.app
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mine.*
import kotlinx.android.synthetic.main.partial_mine_item.view.*
import org.jetbrains.anko.doAsync

class MineActivity : BaseActivity() {
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
        historyViewHolder.assignViews()
    }

    private fun initData() {
        historyViewHolder.initData()
    }

    private fun refreshData() {
        historyViewHolder.refreshData()
    }

    companion object {
        private const val REQUEST_CODE_MINE = 20002

        fun launch(context: Context, bundle: Bundle? = null) {
            context.startActivity(Intent(context, MineActivity::class.java), bundle)
        }
    }

    class HistoryViewHolder(private val root: View, onItemClick: (Post) -> Unit) {
        private val adapter by lazy {
            HistoryAdapter().apply {
                setOnItemClickListener { adapter, view, position ->
                    val post = (adapter as MainAdapter).data.getOrNull(position)
                            ?: return@setOnItemClickListener
                    onItemClick(post)
                }
                setOnItemLongClickListener { adapter, view, position ->
                    val post = (adapter as MainAdapter).data.getOrNull(position)
                            ?: return@setOnItemLongClickListener true

                    Snackbar.make(root.recycler_view, R.string.confirm_delete_post, Snackbar.LENGTH_SHORT).setAction(R.string.delete, {
                        doAsync {
                            root.app.db.postDao().delete(post)
                        }
                        adapter.remove(position)
                    }).show()
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
