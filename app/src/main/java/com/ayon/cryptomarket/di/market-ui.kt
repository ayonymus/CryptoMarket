package com.ayon.cryptomarket.di

import com.ayon.cryptomarket.usecase.SubscribeToMarketDataUseCase
import com.ayon.cryptomarket.ui.market.MarketViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val marketUiModule = module {
    viewModelOf(::MarketViewModel)

    factory { SubscribeToMarketDataUseCase(get(), get(), get(), get()) }
}