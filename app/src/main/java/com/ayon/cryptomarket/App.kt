package com.ayon.cryptomarket

import android.app.Application
import com.ayon.cryptomarket.di.*

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                appModule,
                networkModule,
                marketNetworkModule,
                marketDataModule,
                marketUiModule,
                preferencesModule,
                tokenDataModule
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}