package com.fanhl.kona.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.fanhl.kona.R
import kotlinx.android.synthetic.main.activity_mine.*

class MineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        assignViews()
    }

    private fun assignViews() {
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    companion object {
        fun launch(context: Context, bundle: Bundle? = null) {
            context.startActivity(Intent(context, MineActivity::class.java), bundle)
        }
    }
}
