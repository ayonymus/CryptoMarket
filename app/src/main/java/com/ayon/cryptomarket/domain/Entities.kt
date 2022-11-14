package com.ayon.cryptomarket.domain

typealias Symbol = String

data class TradingPair(
    val symbol1: Symbol,
    val symbol2: Symbol
)

data class TradeDetails(
    val tradingPair: TradingPair,
    val price: Double,
    val changePercentage: Double,
)

data class TokenDetails(
    val symbol: Symbol,
    val name: String,
    val logo: String
)