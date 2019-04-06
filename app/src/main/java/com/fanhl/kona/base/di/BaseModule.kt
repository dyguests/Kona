package com.fanhl.kona.base.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BaseModule {
    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()
}