package com.janewaitara.medec

import android.app.Application
import android.content.Context
import com.janewaitara.medec.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    companion object{
        private lateinit var  instance: App

        fun getAppContext(): Context = instance

    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        initializeKoin()
    }
    /**
     * Dependency injection with Koin*/
    private fun initializeKoin() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext( this@App)

            modules(
                listOf(
                    applicationModule,
                    presentationModule
                )
            )
        }
    }
}