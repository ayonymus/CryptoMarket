package com.ayon.cryptomarket.ui.market

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.ayon.cryptomarket.domain.Trade
import com.ayon.cryptomarket.domain.TradeDetails
import com.ayon.cryptomarket.domain.TradingPair
import org.koin.androidx.compose.koinViewModel

@Composable
fun MarketRoute(
    navController: NavHostController,
    viewModel: MarketViewModel = koinViewModel()
) {

    val lifecycleEvent = rememberLifecycleEvent()

    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            viewModel.handleIntent(MarketIntent.StartListen)
        }
        if (lifecycleEvent == Lifecycle.Event.ON_PAUSE) {
            viewModel.handleIntent(MarketIntent.StopListen)        }
    }

    MarketScreen(
        state = viewModel.marketFlow.collectAsState(),
        onFilterChange = { viewModel.handleIntent(MarketIntent.Filter(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(state: State<MarketState>, onFilterChange: (text: String) -> Unit) {
    val filterState = remember { mutableStateOf(TextFieldValue(state.value.filter)) }
    onFilterChange(filterState.value.text)
    Scaffold(
        topBar =  { TopAppBar(title = { Text("Market Rates") }) },
        content = { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {

                FilterView(
                    state = filterState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Text(
                    modifier = Modifier,
                    text = "Last updated: " + state.value.lastUpdated,
                    fontSize = 12.sp,
                )
                MarketList(state = state.value, contentPadding)
            }
        }
    )
}

@Composable
fun MarketList(
    state: MarketState,
    contentPadding: PaddingValues,
    ) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(contentPadding)
    ) {
        if (state.isFirstLoad) {
            items(10) {
                MarketPlaceholderItem()
            }
        } else {
            items(state.filteredData) { item ->
                MarketItem(item)
            }
        }
    }
}

@Preview
@Composable
fun MarketListScreenPreview() {
    val marketSate = MarketState(
        tradeData = List(30) { i ->
            Trade(
                TradeDetails(
                    TradingPair("BTC $i ", "$i USD"),
                    price = 100.0 * i,
                    changePercentage = 20.0 + i
                ),
                null
            )
        }
    )
    val state = mutableStateOf(marketSate)
    MarketScreen(state) { }
}

// TODO move
@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return state
}