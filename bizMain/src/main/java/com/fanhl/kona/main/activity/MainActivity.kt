package com.fanhl.kona.main.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.fanhl.kona.common.activity.BaseActivity
import com.fanhl.kona.main.screen.MainScreen
import com.fanhl.kona.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel = viewModel)
        }
    }
} 