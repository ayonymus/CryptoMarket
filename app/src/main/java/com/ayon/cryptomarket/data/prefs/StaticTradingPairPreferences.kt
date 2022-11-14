package com.ayon.cryptomarket.data.prefs

import com.ayon.cryptomarket.domain.TradingPair

class StaticTradingPairPreferences: TradingPairPreferences {

    override suspend fun getTradingPairs(): List<TradingPair> {
        return defaultTradingPairs
    }

    companion object {

        val defaultTradingPairs = listOf(
            "BTC", "ETH", "CHSB", "LTC", "XRP", "DSH",
            "RRT", "EOS", "SAN", "DAT", "SNT", "DOGE",
            "LUNA", "MATIC", "NEXO", "OCEAN", "BEST",
            "AAVE", "PLU", "FIL")
            .map { symbol -> TradingPair(symbol, "USD") }
    }
}