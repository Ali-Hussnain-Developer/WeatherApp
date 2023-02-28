package com.example.weatherapp.contextClass

import android.app.Application
import android.content.Context

class ContextClass : Application() {
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {

        lateinit var mContext: Context
        fun getContext(): Context {
            return mContext
        }
    }
}
