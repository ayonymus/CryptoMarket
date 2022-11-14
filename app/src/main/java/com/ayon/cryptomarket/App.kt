package com.ayon.cryptomarket

import android.app.Application
import com.ayon.cryptomarket.di.marketDataModule
import com.ayon.cryptomarket.di.marketNetworkModule
import com.ayon.cryptomarket.di.networkModule
import com.ayon.cryptomarket.di.preferencesModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                networkModule,
                marketNetworkModule,
                marketDataModule,
                preferencesModule
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}