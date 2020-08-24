package com.janewaitara.medec

import android.app.Application
import android.content.Context
import android.content.res.Resources

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
    }
}