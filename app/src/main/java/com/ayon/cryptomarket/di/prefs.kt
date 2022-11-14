package com.ayon.cryptomarket.di

import com.ayon.cryptomarket.data.prefs.StaticTradingPairPreferences
import com.ayon.cryptomarket.data.prefs.TradingPairPreferences
import org.koin.dsl.module

val preferencesModule = module {
    provideTradingPreferences()
}

fun provideTradingPreferences(): TradingPairPreferences {
    return StaticTradingPairPreferences()
}
