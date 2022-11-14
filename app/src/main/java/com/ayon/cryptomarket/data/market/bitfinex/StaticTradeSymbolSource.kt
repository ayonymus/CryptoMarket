package com.ayon.cryptomarket.data.market.bitfinex

import com.ayon.cryptomarket.data.prefs.StaticTradingPairPreferences.Companion.defaultTradingPairs
import com.ayon.cryptomarket.domain.TradingPair

// TODO implement non-static
class StaticTradeSymbolSource: BitfinexTradeSymbolSource {

    override suspend fun getTradeSymbol(tradingPair: TradingPair): TradingSymbol {
        return defaultTradingSymbols[defaultTradingPairs.indexOfFirst { it == tradingPair }]
    }

    override suspend fun getTradingPair(tradingSymbol: TradingSymbol): TradingPair {
        return defaultTradingPairs[defaultTradingSymbols.indexOfFirst { it == tradingSymbol }]
    }

    companion object {
        val defaultTradingSymbols = listOf(
            "tBTCUSD","tETHUSD","tCHSB:USD","tLTCUSD","tXRPUSD","tDSHUSD",
            "tRRTUSD","tEOSUSD","tSANUSD","tDATUSD","tSNTUSD","tDOGE:USD",
            "tLUNA:USD","tMATIC:USD","tNEXO:USD","tOCEAN:USD","tBEST:USD",
            "tAAVE:USD","tPLUUSD","tFILUSD"
        )
    }

}