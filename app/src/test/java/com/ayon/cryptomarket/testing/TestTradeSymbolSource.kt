package com.ayon.cryptomarket.testing

import com.ayon.cryptomarket.data.market.bitfinex.BitfinexTradeSymbolSource
import com.ayon.cryptomarket.data.market.bitfinex.TradingSymbol
import com.ayon.cryptomarket.domain.TradingPair

class TestTradeSymbolSource: BitfinexTradeSymbolSource {

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

        val defaultTradingPairs = listOf(
            "BTC", "ETH", "CHSB", "LTC", "XRP", "DSH",
            "RRT", "EOS", "SAN", "DAT", "SNT", "DOGE",
            "LUNA", "MATIC", "NEXO", "OCEAN", "BEST",
            "AAVE", "PLU", "FIL")
            .map { symbol -> TradingPair(symbol, "USD") }
    }

}