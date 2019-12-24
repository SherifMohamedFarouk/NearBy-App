package com.sherif.nearbyapp

import android.app.Application
import android.content.Context
import com.sherif.nearbyapp.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application(){

    companion object{
        lateinit var appContext : Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        startKoin {
            androidContext(this@MyApplication)
            modules(appModules)
        }
    }

}