package com.benmohammad.nynuze.dagger.modules

import android.content.Context
import androidx.room.Room
import com.benmohammad.nynuze.DATABASE_NAME
import com.benmohammad.nynuze.dagger.scope.ApplicationScope
import com.benmohammad.nynuze.data.NYDatabase
import com.benmohammad.nynuze.data.dao.NewsDao
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Provides
    @ApplicationScope
    fun provideDatabase(context: Context): NYDatabase {
        return Room.databaseBuilder(context, NYDatabase::class.java, DATABASE_NAME)
                .build()
    }

    @Provides
    @ApplicationScope
    fun provideNewsDao(zapDatabase: NYDatabase): NewsDao {
        return zapDatabase.homeNewsDao()
    }
}