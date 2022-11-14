package com.ayon.cryptomarket.usecase

import com.ayon.cryptomarket.data.market.FlowingMarketRepository
import com.ayon.cryptomarket.data.prefs.TradingPairPreferences
import com.ayon.cryptomarket.data.token.TokenDetailsRepository
import com.ayon.cryptomarket.domain.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

val TOKENS = listOf("BTC", "ETH", "CHSB")
val TRADING_PAIRS = TOKENS
    .map { symbol -> TradingPair(symbol, "USD") }

val TRADE_DETAILS = TRADING_PAIRS.map {
    TradeDetails(it, 2.0, 50.0)
}

val TOKEN_MAP = TOKENS.associateWith { TokenDetails(it, it, it) }

internal class SubscribeToMarketDataUseCaseTest {

    private var market = mock<FlowingMarketRepository>()
    private var tradingPairs = mock<TradingPairPreferences>()
    private var tokenDetails = mock<TokenDetailsRepository>()

    private lateinit var useCase: SubscribeToMarketDataUseCase

    @Before
    fun setUp() {
        useCase = SubscribeToMarketDataUseCase(market, tradingPairs, tokenDetails)
    }

    @Test
    fun `subscribe to market with trade pairs from preferences, no token details`() {
        runBlocking {
            whenever(tradingPairs.getTradingPairs()).thenReturn(TRADING_PAIRS)
            whenever(tokenDetails.fetchTokenDetails(any())).thenReturn(Result.success(emptyMap()))
            whenever(market.getTradeDetails(any())).thenReturn(flowOf(Result.success(TRADE_DETAILS)))

            val expected = TRADE_DETAILS.map { Trade(it, null) }

            val marketFlow = useCase()

            assertEquals(expected, marketFlow.single())
        }
    }

    @Test
    fun `subscribe to market with trade pairs from preferences, token details failure`() {
        runBlocking {
            whenever(tradingPairs.getTradingPairs()).thenReturn(TRADING_PAIRS)
            whenever(tokenDetails.fetchTokenDetails(any())).thenReturn(Result.failure(IOException()))
            whenever(market.getTradeDetails(any())).thenReturn(flowOf(Result.success(TRADE_DETAILS)))

            val expected = TRADE_DETAILS.map { Trade(it, null) }

            val marketFlow = useCase()

            assertEquals(expected, marketFlow.single())
        }
    }

    @Test
    fun `subscribe to market with trade pairs from preferences, with token details`() {
        runBlocking {
            whenever(tradingPairs.getTradingPairs()).thenReturn(TRADING_PAIRS)
            whenever(tokenDetails.fetchTokenDetails(any())).thenReturn(Result.success(TOKEN_MAP))
            whenever(market.getTradeDetails(any())).thenReturn(flowOf(Result.success(TRADE_DETAILS)))

            val expected = TRADE_DETAILS.map { Trade(it, TOKEN_MAP[it.tradingPair.symbol1]) }

            val marketFlow = useCase()

            assertEquals(expected, marketFlow.single())
        }
    }

    @Test
    fun `subscribe to market with trade pairs from preferences, error`() {
        runBlocking {
            val error = IOException()
            whenever(tradingPairs.getTradingPairs()).thenReturn(TRADING_PAIRS)
            whenever(tokenDetails.fetchTokenDetails(any())).thenReturn(Result.success(TOKEN_MAP))
            whenever(market.getTradeDetails(any())).thenReturn(flowOf(Result.failure(error)))

            val expected = Result.failure<List<TradeDetails>>(error)

            val marketFlow = useCase()

            assertEquals(expected, marketFlow.single())
        }
    }
}