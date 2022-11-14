package com.ayon.cryptomarket.data.token.cmc

import com.ayon.cryptomarket.data.token.TokenDetailsRepository
import com.ayon.cryptomarket.data.token.cmc.local.TokenDetailsDao
import com.ayon.cryptomarket.data.token.cmc.local.toDatabaseEntity
import com.ayon.cryptomarket.data.token.cmc.local.toTokenDetails
import com.ayon.cryptomarket.data.token.cmc.remote.CoinMarketCapService
import com.ayon.cryptomarket.data.token.cmc.remote.toTokenDetails
import com.ayon.cryptomarket.data.utils.toResult
import com.ayon.cryptomarket.domain.Symbol
import com.ayon.cryptomarket.domain.TokenDetails

class CMCTokenDetailsRepository(
    private val service: CoinMarketCapService,
    private val tokenDetailsDao: TokenDetailsDao
): TokenDetailsRepository {

    private var tokenDetailsCache: Map<Symbol, TokenDetails>? = null

    /**
     * Check order:
     * 1. in-memory cache
     * 2. local database -> update cache
     * 3. remote source -> update cache -> update database
     */
    override suspend fun fetchTokenDetails(symbols: List<Symbol>): Result<Map<Symbol, TokenDetails>> {
        if (tokenDetailsCache == null) {
            tokenDetailsCache = tokenDetailsDao.getTokenDetails()
                .associateBy({ it.symbol }, { it.toTokenDetails() })
        }
        tokenDetailsCache?.let {
            if (it.keys.containsAll(symbols)) {
                return Result.success(it)
            }
        }

        return fetchRemote(symbols)
                .onSuccess { remoteResult ->
                    tokenDetailsCache = remoteResult
                    tokenDetailsDao.insertAll(remoteResult.values.map { it.toDatabaseEntity() })
                }
    }

    private suspend fun fetchRemote(symbols: List<Symbol>): Result<Map<Symbol, TokenDetails>> {
        return service.getTokenInfo(symbols.joinToString(separator = ",") { it })
            .toResult()
            .map { response ->
                val tokenMap = response.data.mapValues {
                    it.value[0].toTokenDetails()
                }
                return@map tokenMap
            }
    }
}


