package com.fanhl.kona.di

import com.fanhl.kona.base.di.BaseModule
import com.fanhl.kona.base.ui.InjectedActivity
import com.fanhl.kona.net.di.NetModule
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        BaseModule::class,
        NetModule::class
    ]
)
interface AppComponent : AndroidInjector<InjectedActivity>