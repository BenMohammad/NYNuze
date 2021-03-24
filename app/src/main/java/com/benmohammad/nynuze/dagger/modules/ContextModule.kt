package com.benmohammad.nynuze.dagger.modules

import android.content.Context
import com.benmohammad.nynuze.dagger.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {
    @Provides
    @ApplicationScope
    fun provideContext(): Context {
        return context
    }
}