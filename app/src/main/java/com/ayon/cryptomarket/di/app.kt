package com.ayon.cryptomarket.di

import com.ayon.cryptomarket.framework.SystemTimeProvider
import com.ayon.cryptomarket.framework.TimeProvider
import org.koin.dsl.module

val appModule = module {
    factory { provideTimeProvider() }
}

fun provideTimeProvider(): TimeProvider = SystemTimeProvider()