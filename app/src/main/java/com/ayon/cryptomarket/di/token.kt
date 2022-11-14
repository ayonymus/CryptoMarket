package com.ayon.cryptomarket.di

import com.ayon.cryptomarket.data.token.TokenDetailsRepository
import com.ayon.cryptomarket.domain.Symbol
import com.ayon.cryptomarket.domain.TokenDetails
import org.koin.dsl.module

val tokenDataModule = module {
    provideTokeDetailsRepository()
}

fun provideTokeDetailsRepository(): TokenDetailsRepository {
    return object : TokenDetailsRepository {
        override suspend fun fetchTokenDetails(symbols: List<Symbol>): Result<Map<Symbol, TokenDetails>> {
            return Result.success(emptyMap())
        }

    }
}
