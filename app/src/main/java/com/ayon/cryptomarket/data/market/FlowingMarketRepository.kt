package com.ayon.cryptomarket.data.market

import com.ayon.cryptomarket.domain.TradeDetails
import com.ayon.cryptomarket.domain.TradingPair

interface FlowingMarketRepository{
    suspend fun getTradeDetails(tradingPairs: List<TradingPair>): Flow<List<TradeDetails>>
}
