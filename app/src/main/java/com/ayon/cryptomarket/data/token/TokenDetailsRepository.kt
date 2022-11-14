package com.ayon.cryptomarket.data.token

import com.ayon.cryptomarket.domain.Symbol
import com.ayon.cryptomarket.domain.TokenDetails

interface TokenDetailsRepository {

    suspend fun fetchTokenDetails(symbols: List<Symbol>): Result<Map<Symbol, TokenDetails>>
}