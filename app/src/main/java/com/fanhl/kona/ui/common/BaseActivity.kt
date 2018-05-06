package com.fanhl.kona.ui.common

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.fanhl.kona.App

abstract class BaseActivity : AppCompatActivity() {
    val app: App
        get() {
            return application as App
        }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
