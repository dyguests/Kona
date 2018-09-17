package com.fanhl.kona.ui.gallery

import android.app.Activity
import android.app.WallpaperManager
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanhl.kona.R
import com.fanhl.kona.model.Post
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.ui.main.MainActivity
import com.fanhl.kona.util.FileUtils
import com.fanhl.kona.util.observe
import com.fanhl.kona.util.rxClicks
import com.fanhl.util.SpanUtils
import com.fanhl.util.px
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.item_tag.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

/**
 * 图库页面
 */
class GalleryActivity : BaseActivity() {
    private val fullscreenConstraintSet by lazy { ConstraintSet().apply { clone(this@GalleryActivity, R.layout.activity_gallery) } }
    private val normalConstraintSet by lazy { ConstraintSet().apply { clone(this@GalleryActivity, R.layout.activity_gallery_alt) } }

    private val tagAdapter by lazy {
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_tag) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
                helper?.itemView?.tv_tag?.text = SpanUtils()
                        .append(item ?: return)
                        .setShadow(2.px.toFloat(), 0f, 0f, R.color.text_shadow)
                        .create()
            }
        }.apply {
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, _, position ->
                //                setResult(Activity.RESULT_OK, intent.apply {
                //                    putExtra(RESULT_DATA_TAG, adapter.data[position] as String)
                //                })
                MainActivity.launch(this@GalleryActivity, adapter.data[position] as String)
                finish()
            }
        }
    }

    private lateinit var viewModel: ViewModel

    private var fullscreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = uiHide
        setContentView(R.layout.activity_gallery)

        prepareData()
        assignViews()
        initData()
    }

    private fun prepareData() {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModel.post.value = intent.getParcelableExtra(EXTRA_POST)

        doAsync {
            val post = app.db.postDao().findByName(viewModel.post.value?.id ?: return@doAsync)
            viewModel.post.value = viewModel.post.value?.apply {
                favorite = post.favorite
            }
        }
    }

    private fun assignViews() {
        Observable.merge(
                constraint_layout.rxClicks,
                photo_view.rxClicks
        ).subscribe { toggle() }

        fab_favorite.setOnClickListener {
            viewModel.post.value = viewModel.post.value?.apply {
                favorite = favorite != true
            }
            doAsync {
                app.db.postDao().insertOrReplaceAll(viewModel.post.value ?: return@doAsync)
            }
        }

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

        fab_wallpaper.setOnLongClickListener {
            val bitmap = (photo_view.drawable as? BitmapDrawable)?.bitmap
                    ?: return@setOnLongClickListener true
            doAsync {
                WallpaperManager
                        .getInstance(this@GalleryActivity)
                        .setBitmap(bitmap)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    WallpaperManager
                            .getInstance(this@GalleryActivity)
                            .setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                }
            }
            toast(R.string.wallpaper_will_been_set_soon)

            true
        }

        viewModel.post.observe(this) {
            Glide.with(photo_view)
                    .load(it?.fileUrl ?: return@observe)
//                    .load("http://img0.imgtn.bdimg.com/it/u=4236860535,1526027473&fm=27&gp=0.jpg")
                    .thumbnail(.1f)
                    .apply(RequestOptions().dontTransform())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            FileUtils.save(it.id,(resource as? BitmapDrawable)?.bitmap ?: return false)
                            return false
                        }
                    })
                    .into(photo_view)

            recycler_view.adapter = tagAdapter
            tagAdapter.setNewData(it?.tags?.split(" "))

            fab_favorite.setImageDrawable(ContextCompat.getDrawable(this@GalleryActivity, if (it?.favorite == true) R.drawable.ic_baseline_favorite_24px else R.drawable.ic_baseline_favorite_border_24px))
        }
    }

    private fun initData() {
        doAsync {
            app.db.postDao().insertOrIgnoreAll(viewModel.post.value ?: return@doAsync)
        }

    }

    private fun toggle() {
        fullscreen = !fullscreen
        if (fullscreen) {
            window.decorView.systemUiVisibility = uiHide
            TransitionManager.beginDelayedTransition(constraint_layout)
            fullscreenConstraintSet.applyTo(constraint_layout)
        } else {
            window.decorView.systemUiVisibility = uiShow
            TransitionManager.beginDelayedTransition(constraint_layout)
            normalConstraintSet.applyTo(constraint_layout)
        }
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

        fun launch(activity: Activity, post: Post) {
            activity.startActivity(
                    Intent(activity, GalleryActivity::class.java).apply {
                        putExtra(EXTRA_POST, post)
                    }
            )
        }
    }

    class ViewModel : android.arch.lifecycle.ViewModel() {
        /** 输入Post */
        val post by lazy { MutableLiveData<Post>() }
    }
}
