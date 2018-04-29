package com.fanhl.kona.ui.gallery

import android.app.Activity
import android.app.WallpaperManager
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanhl.kona.R
import com.fanhl.kona.net.model.Post
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.util.rxClicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.item_tag.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class GalleryActivity : BaseActivity() {
    private val adapter by lazy {
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_tag) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
                helper?.itemView?.tv_tag?.text = item
            }
        }.apply {
            onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                setResult(Activity.RESULT_OK, intent.apply {
                    putExtra(RESULT_DATA_TAG, adapter.data[position] as String)
                })
                finish()
            }
        }
    }

    private lateinit var viewModel: ViewModel

    private var fullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toggle()
        setContentView(R.layout.activity_gallery)

        prepareData()
        assignViews()
        initData()
    }

    private fun prepareData() {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModel.post.value = intent.getParcelableExtra(EXTRA_POST)
    }

    private fun assignViews() {
        Observable.merge(
                constraint_layout.rxClicks,
                photo_view.rxClicks
        ).subscribe { toggle() }

        fab_wallpaper.setOnClickListener {
            val bitmap = (photo_view.drawable as? BitmapDrawable)?.bitmap
                    ?: return@setOnClickListener
            doAsync {
                WallpaperManager
                        .getInstance(this@GalleryActivity)
                        .setBitmap(bitmap)
            }
            toast(R.string.wallpaper_will_been_set_soon)
        }
    }

    private fun initData() {
        Glide.with(photo_view)
                .load(viewModel.post.value?.fileUrl ?: return)
                .apply(RequestOptions().dontTransform())
                .into(photo_view)

        recycler_view.adapter = adapter
        adapter.setNewData(viewModel.post.value?.tags?.split(" "))
    }

    private fun toggle() {
        fullscreen = !fullscreen
        window.decorView.systemUiVisibility = if (fullscreen) uiHide else uiShow
    }

    companion object {
        private const val EXTRA_POST = "EXTRA_POST"
         const val RESULT_DATA_TAG = "RESULT_DATA_TAG"

        private const val uiHide = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        private const val uiShow = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        fun launchForResult(activity: Activity, requestCode: Int, post: Post) {
            activity.startActivityForResult(
                    Intent(activity, GalleryActivity::class.java).apply {
                        putExtra(EXTRA_POST, post)
                    },
                    requestCode
            )
        }
    }

    class ViewModel : android.arch.lifecycle.ViewModel() {
        /** 输入Post */
        val post by lazy { MutableLiveData<Post>() }
    }
}
