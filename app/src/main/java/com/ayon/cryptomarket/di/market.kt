package com.ayon.cryptomarket.di

import com.ayon.cryptomarket.data.market.FlowingMarketRepository
import com.ayon.cryptomarket.data.market.bitfinex.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val marketDataModule = module {
    factory { provideGsonConverterFactory() }
    factory { provideBitfinexApi(get(), get()) }
    factory { provideBitfinexTradeSymbolSource() }
    single<FlowingMarketRepository> {
        BitfinexMarketRepository(get(), get())
    }
}

fun provideGsonConverterFactory(): GsonConverterFactory {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.registerTypeAdapter(BitfinexTicker::class.java, BitfinexTickerDeserializer())
    return GsonConverterFactory.create(gsonBuilder.create())
}

fun provideBitfinexApi(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): BitfinexApi {

    return Retrofit.Builder()
        .baseUrl("https://api-pub.bitfinex.com/")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(BitfinexApi::class.java)
}

fun provideBitfinexTradeSymbolSource(): BitfinexTradeSymbolSource {
    return StaticTradeSymbolSource()
}
