package com.fanhl.kona.base.di

import androidx.lifecycle.ViewModelProvider
import com.fanhl.kona.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {
    @Binds
    @Singleton
    abstract fun bindViewModelFactory(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

//    @Binds
//    @IntoMap
//    @ViewModelKey(JobListViewModel::class)
//    abstract fun bindListViewModel(jobListViewModel: JobListViewModel): ViewModel
}