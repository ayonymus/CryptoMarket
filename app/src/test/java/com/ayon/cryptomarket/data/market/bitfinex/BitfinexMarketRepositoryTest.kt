package com.ayon.cryptomarket.data.market.bitfinex

import com.ayon.cryptomarket.data.utils.NetworkError
import com.ayon.cryptomarket.domain.TradeDetails
import com.ayon.cryptomarket.domain.TradingPair
import com.ayon.cryptomarket.testing.TestTradeSymbolSource
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

val TRADE_SYMBOLS = listOf("tBTCUSD","tETHUSD","tCHSB:USD",)
val TOKENS = listOf("BTC", "ETH", "CHSB")
val TRADING_PAIRS = TOKENS.map { symbol -> TradingPair(symbol, "USD") }

val BFXTICKERS = TRADE_SYMBOLS.map { BitfinexTicker(it, 10.0, 20.0) }

internal class BitfinexMarketRepositoryTest {

    private var api: BitfinexApi = mock()
    private var tradeSymbol: BitfinexTradeSymbolSource = TestTradeSymbolSource() // this case its easier to use a static data source than setting up mocks

    private lateinit var repo: BitfinexMarketRepository

    @Before
    fun setUp() {
        repo = BitfinexMarketRepository(api, tradeSymbol)
    }

    @Test
    fun `fetch data from api when no error`() {
        runBlocking {
            whenever(api.getTickers(any())).thenReturn(Response.success(BFXTICKERS))

            val expected = Result.success(TRADING_PAIRS.map { TradeDetails(it, 10.0, 20.0) })
            val marketFlow = repo.getTradeDetails(TRADING_PAIRS)

            assertEquals(expected, marketFlow.single())
        }
    }

    @Test
    fun `fetch data from api when error`() {
        runBlocking {
            val errorResponse: ResponseBody = "some error".toResponseBody()
            whenever(api.getTickers(any())).thenReturn(Response.error(400, errorResponse))

            val expected = Result.failure<List<TradeDetails>>(NetworkError())
            val marketFlow = repo.getTradeDetails(TRADING_PAIRS)

            assertTrue(expected::class == marketFlow.single()::class)
        }
    }
}