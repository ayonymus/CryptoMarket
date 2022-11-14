package com.ayon.cryptomarket.data.prefs

import com.ayon.cryptomarket.domain.TradingPair

interface TradingPairPreferences {

    suspend fun getTradingPairs(): List<TradingPair>

}
