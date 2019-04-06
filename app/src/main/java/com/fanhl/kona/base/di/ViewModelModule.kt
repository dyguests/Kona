package com.fanhl.kona.base.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ViewModelModule {
    @Binds
    @Singleton
    abstract fun bindViewModelFactory(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}