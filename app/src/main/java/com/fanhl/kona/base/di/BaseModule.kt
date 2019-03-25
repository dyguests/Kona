package com.fanhl.kona.base.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
class BaseModule {
    @Provides
    fun provideGson(): Gson = Gson()
}