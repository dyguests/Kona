package com.fanhl.database.di

import android.content.Context
import androidx.room.Room
import com.fanhl.database.KonaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): KonaDatabase {
        return Room.databaseBuilder(
            context,
            KonaDatabase::class.java,
            "kona_database"
        ).build()
    }

    @Provides
    fun provideCoverDao(database: KonaDatabase) = database.coverDao()
} 