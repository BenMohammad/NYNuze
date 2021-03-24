package com.benmohammad.nynuze

import android.app.Application
import android.content.Context
import com.benmohammad.nynuze.dagger.DaggerMainComponent
import com.benmohammad.nynuze.dagger.MainComponent
import com.benmohammad.nynuze.dagger.modules.ContextModule

class NyApp: Application() {

    lateinit var mainComponent: MainComponent
    private set

    override fun onCreate() {
        super.onCreate()
        mainComponent = DaggerMainComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    companion object {
        fun getApp(context: Context): NyApp {
            return context.applicationContext as NyApp
        }
    }
}