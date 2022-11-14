package com.ayon.cryptomarket.di

import com.ayon.cryptomarket.data.token.TokenDetailsRepository
import com.ayon.cryptomarket.data.token.cmc.CMCTokenDetailsRepository
import com.ayon.cryptomarket.data.token.cmc.remote.CoinMarketCapService
import com.ayon.cryptomarket.data.token.cmc.local.TokenDetailsDatabase
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val tokenDataModule = module {
    single<TokenDetailsRepository> {
        CMCTokenDetailsRepository(get(), get())
    }
    single { provideCMCApi(get(), get()) }
}

fun provideCMCApi(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): CoinMarketCapService {

    return Retrofit.Builder()
        .baseUrl("https://pro-api.coinmarketcap.com/")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(CoinMarketCapService::class.java)
}


val tokenDatabaseModule = module {
    single {
        TokenDetailsDatabase.getDatabase(get())
    }

    single { provideTokenDetailsDao(get()) }

}

fun provideTokenDetailsDao(db: TokenDetailsDatabase) = db.tokenDetailsDao()
