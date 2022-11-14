package com.ayon.cryptomarket.usecase

import com.ayon.cryptomarket.data.market.FlowingMarketRepository
import com.ayon.cryptomarket.data.prefs.TradingPairPreferences
import com.ayon.cryptomarket.data.token.TokenDetailsRepository
import com.ayon.cryptomarket.domain.Trade
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubscribeToMarketDataUseCase(
    private val marketRepository: FlowingMarketRepository,
    private val tradingPairPreference: TradingPairPreferences,
    private val tokenDetailsRepository: TokenDetailsRepository
) {

    suspend operator fun invoke(): Flow<Result<List<Trade>>> {
        val tradingPairs = tradingPairPreference.getTradingPairs()
        val symbols = tradingPairs.map { it.symbol1 }

        val tokenDetails = tokenDetailsRepository.fetchTokenDetails(symbols)
            .fold(onSuccess = { it }, onFailure = { emptyMap() })

        return marketRepository.getTradeDetails(tradingPairs)
            .map { tradeListResult ->
                tradeListResult.map { tradeList ->
                    tradeList.map { trade ->
                        Trade(
                            tradeDetails = trade,
                            tokenDetails = tokenDetails[trade.tradingPair.symbol1]
                        )
                    }
                }
            }
    }

}
