package com.example.local.di

import android.content.Context
import androidx.room.Room
import com.example.local.AlgarTechDatabase
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
    fun providesDatabaseDatabase(
        @ApplicationContext context: Context,
    ): AlgarTechDatabase = Room.databaseBuilder(
        context,
        AlgarTechDatabase::class.java,
        "algartech-challenge-database",
    ).build()
}