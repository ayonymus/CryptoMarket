package com.ayon.cryptomarket.data.market.bitfinex

import com.ayon.cryptomarket.domain.TradingPair

interface BitfinexTradeSymbolSource {

    suspend fun getTradeSymbol(tradingPair: TradingPair): TradingSymbol
    suspend fun getTradingPair(tradingSymbol: TradingSymbol): TradingPair

}