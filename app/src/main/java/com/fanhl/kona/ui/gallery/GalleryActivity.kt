package com.fanhl.kona.ui.gallery

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fanhl.kona.R
import com.fanhl.kona.net.model.Post
import com.fanhl.kona.ui.common.BaseActivity
import com.fanhl.kona.util.rxClicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_gallery.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast


class GalleryActivity : BaseActivity() {
    private val mHidePart2Runnable = Runnable {
        photo_view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private var mVisible: Boolean = false

    /** 输入Post */
    private var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_gallery)

        post = intent.getParcelableExtra(EXTRA_POST)

        assignViews()
        initData()
    }

    private fun assignViews() {
        Observable.merge(
                constraint_layout.rxClicks,
                photo_view.rxClicks
        ).subscribe { toggle() }

        Glide.with(photo_view)
                .load(post?.fileUrl ?: return)
                .apply(RequestOptions().dontTransform())
                .into(photo_view)

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
//        mVisible = true
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        mVisible = false

//        mHideHandler.removeCallbacks(mShowPart2Runnable)
    }

    private fun show() {
        photo_view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

//        mHideHandler.removeCallbacks(mHidePart2Runnable)
    }

    companion object {

        private const val EXTRA_POST = "EXTRA_POST"

        fun launch(context: Context, post: Post) {
            context.startActivity(Intent(context, GalleryActivity::class.java).apply {
                putExtra(EXTRA_POST, post)
            })
        }
    }
}
