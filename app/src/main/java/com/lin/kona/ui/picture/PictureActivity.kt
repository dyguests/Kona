package com.lin.kona.ui.picture

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lin.kona.R
import com.lin.kona.databinding.ActivityPictureBinding
import com.lin.kona.model.Post
import com.lin.kona.ui.common.BaseActivity
import com.lin.kona.util.loadBy
import com.lin.kona.util.toast
import kotlinx.coroutines.launch

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PictureActivity : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[PictureViewModel::class.java] }
    private val binding by lazy { ActivityPictureBinding.inflate(layoutInflater) }

    private val hideHandler = Handler(Looper.myLooper()!!)

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            binding.photoView.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            binding.photoView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        binding.fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen = true

    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)
            }
            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
        }
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.post.observe(this) {
            binding.photoView.loadBy(it.fileUrl) {
                thumbnail(Glide.with(binding.photoView).load(it.previewUrl))
                apply(RequestOptions().dontTransform())
            }
        }

        binding.photoView.setOnClickListener { toggle() }
        binding.photoView.setOnLongClickListener {
            val bitmap = (binding.photoView.drawable as? BitmapDrawable)?.bitmap ?: return@setOnLongClickListener true
            launch {
                WallpaperManager.getInstance(this@PictureActivity).setBitmap(bitmap)
            }
            toast(R.string.wallpaper_will_been_set_soon)
            true
        }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.dummyButton.setOnTouchListener(delayHideTouchListener)

        viewModel.post.value = intent.getParcelableExtra(EXTRA_POST)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        binding.fullscreenContentControls.visibility = View.GONE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            binding.photoView.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            binding.photoView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        private const val EXTRA_POST = "EXTRA_POST"

        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300

        fun launch(fragment: Fragment, post: Post) {
            fragment.startActivity(
                Intent(fragment.activity, PictureActivity::class.java)
                    .apply {
                        putExtra(EXTRA_POST, post)
                    }
            )
        }
    }
}