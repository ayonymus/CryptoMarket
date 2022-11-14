package com.ayon.cryptomarket.data.market.bitfinex

import com.ayon.cryptomarket.data.market.FlowingMarketRepository
import com.ayon.cryptomarket.data.utils.toResult
import com.ayon.cryptomarket.domain.TradeDetails
import com.ayon.cryptomarket.domain.TradingPair
import com.ayon.cryptomarket.framework.pollingFlowOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class BitfinexMarketRepository(
    private val api: BitfinexApi,
    private val tradeSymbol: BitfinexTradeSymbolSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val maxRepeat: Int = 100,
    private val delay: Long = 5000
): FlowingMarketRepository {

    override suspend fun getTradeDetails(tradingPairs: List<TradingPair>): Flow<Result<List<TradeDetails>>> {
        return pollingFlowOf(
            task = { fetch(tradingPairs) },
            maxRepeat = maxRepeat,
            delayMS = delay
        )
            .flowOn(dispatcher)
    }

    private suspend fun fetch(tradingPairs: List<TradingPair>) : Result<List<TradeDetails>> {
        val marketSymbols = tradingPairs.map { tradeSymbol.getTradeSymbol(it) }
        val requestString = marketSymbols.joinToString(separator = ",") { it }

        return api.getTickers(requestString)
            .toResult()
            .map {
                it.map { ticker ->
                    TradeDetails(
                        tradingPair = tradeSymbol.getTradingPair(ticker.symbol),
                        price = ticker.last_price,
                        changePercentage = ticker.daily_change_relative
                    )
                }
            }
    }
}
