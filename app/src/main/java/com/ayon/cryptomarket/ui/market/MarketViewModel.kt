package com.ayon.cryptomarket.ui.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayon.cryptomarket.domain.Trade
import com.ayon.cryptomarket.usecase.SubscribeToMarketDataUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

class MarketViewModel(
    private val marketFlowUseCase: SubscribeToMarketDataUseCase,
): ViewModel() {

    private val _marketFlow = MutableStateFlow(MarketState(emptyList(), emptyList(),false, true))
    val marketFlow: StateFlow<MarketState> = _marketFlow

    private var marketFlowJob: Job? = null

    fun handleIntent(intent: MarketIntent) {
        val newState: MarketState = when (intent) {
            is MarketIntent.StartListen -> startListening()
            is MarketIntent.StopListen -> stopListening()
            is MarketIntent.Filter -> applyFilter(intent.phrase)
        }
        viewModelScope.launch {
            _marketFlow.emit(newState)
        }
    }

    private fun startListening(): MarketState {
        marketFlowJob?.cancel()
        listenMarketUpdates()
        return marketFlow.value.copy(isListening = true)
    }

    private fun stopListening(): MarketState {
        marketFlowJob?.cancel()
        marketFlowJob = null
        return marketFlow.value.copy(isListening = false)
    }

    private fun applyFilter(text: String): MarketState {
        return _marketFlow.value.applyFilter(text)
    }

    private fun listenMarketUpdates() {
        marketFlowJob = viewModelScope.launch {
            marketFlowUseCase()
                .map { result ->
                    result.fold(
                        onSuccess = {
                            return@map  _marketFlow.value.copy(
                                tradeData = it.trades,
                                lastUpdated = it.receivedAt,
                                isFirstLoad = false,
                            ).applyFilter()
                        },
                        onFailure = {
                            Timber.e(it)
                            return@map _marketFlow.value.copy(errorFetchingData = true)
                        }
                    )
                }
                .catch {
                    Timber.e(it)
                    _marketFlow.emit(marketFlow.value.copy(errorFetchingData = true))
                }
                .collect {
                    _marketFlow.emit(it)
                }
        }
    }

    override fun onCleared() {
        marketFlowJob?.cancel()
        super.onCleared()
    }
}

data class MarketState(
    val tradeData: List<Trade> = emptyList(),
    val filteredData: List<Trade> = emptyList(),
    val errorFetchingData: Boolean = false,
    val isFirstLoad: Boolean = true,
    val filter: String = "",
    val lastUpdated: String = "",
    val isListening: Boolean = false
)

fun MarketState.applyFilter(text: String? = null): MarketState {
    val newFilter = if (text != this.filter && text != null) {
        text
    } else {
        this.filter
    }
    return this.copy(
        filter = newFilter,
        filteredData = tradeData.filter { item ->
            item.tokenDetails?.name?.contains(newFilter, ignoreCase = true) == true ||
                item.tradeDetails.tradingPair.symbol1.contains(filter, ignoreCase = true)
        }
    )
}

sealed class MarketIntent{
    object StartListen: MarketIntent()
    object StopListen: MarketIntent()
    data class Filter(val phrase: String): MarketIntent()
}
