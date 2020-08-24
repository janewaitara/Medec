package com.janewaitara.medec

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.janewaitara.medec.di.applicationModule
import com.janewaitara.medec.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    companion object{
        private lateinit var  instance: App
        private lateinit var res: Resources

        fun getAppContext(): Context = instance
        fun getResources() = res
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        res = resources

        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(instance.applicationContext)
            modules(
                listOf(
                    applicationModule,
                    presentationModule
                )
            )
        }
    }
}