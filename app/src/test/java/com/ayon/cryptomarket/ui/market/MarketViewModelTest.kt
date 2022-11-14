package com.ayon.cryptomarket.ui.market

import com.ayon.cryptomarket.domain.MarketUpdate
import com.ayon.cryptomarket.testing.MainDispatcherRule
import com.ayon.cryptomarket.usecase.SubscribeToMarketDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

const val testTime = "12:34:56"

@OptIn(ExperimentalCoroutinesApi::class)
class MarketViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val marketDataUseCase = mock<SubscribeToMarketDataUseCase>()

    private lateinit var viewModel: MarketViewModel

    @Before
    fun setUp() {
        viewModel = MarketViewModel(marketDataUseCase)
    }

    @Test
    fun `when StartListen intent and error then show error`() = runTest {
        val market = viewModel.marketFlow
        val collectJob = launch(UnconfinedTestDispatcher()) { market.collect() }
        viewModel.handleIntent(MarketIntent.StartListen)

        assertEquals(
            MarketState(
                isListening = true,
                isFirstLoad = true,
                errorFetchingData = true
            ),
            market.value)

        collectJob.cancel()
    }

    @Test
    fun `when StartListen intent then start listening to market updates`() = runTest {
        val marketUpdate = MarketUpdate(emptyList(), testTime)
        val market = viewModel.marketFlow
        val collectJob = launch(UnconfinedTestDispatcher()) { market.collect() }
        whenever(marketDataUseCase()).thenReturn(flowOf(Result.success(marketUpdate)))
        viewModel.handleIntent(MarketIntent.StartListen)

        assertEquals(
            MarketState(
                isListening = true,
                lastUpdated = testTime,
                isFirstLoad = false
            ),
            market.value)

        collectJob.cancel()
    }

    @Test
    fun `when StopListen intent then stop listening to market updates`() = runTest {
        val marketUpdate = MarketUpdate(emptyList(), testTime)
        val market = viewModel.marketFlow

        whenever(marketDataUseCase()).thenReturn(flowOf(Result.success(marketUpdate)))
        viewModel.handleIntent(MarketIntent.StartListen)

        val collectJob = launch(UnconfinedTestDispatcher()) { market.collect() }
        viewModel.handleIntent(MarketIntent.StartListen)
        viewModel.handleIntent(MarketIntent.StopListen)

        assertEquals(
            MarketState(
                isListening = false,
                lastUpdated = testTime,
                isFirstLoad = false
            ),
            market.value)

        collectJob.cancel()
    }

}