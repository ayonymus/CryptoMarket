package com.ayon.cryptomarket.ui.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.ayon.cryptomarket.R
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
        topBar =  { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) },
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

                if (state.value.hasErrorFetching) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color.Red)
                            .clip(RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center


                    ) {
                        Text(
                            modifier = Modifier,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.generic_error)
                        )

                    }

                }
                Text(
                    modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.last_updated, state.value.lastUpdated),
                        fontSize = 12.sp,
                    )

                MarketList(state = state.value)
            }
        }
    )
}

@Composable
fun MarketList(
    state: MarketState,
    ) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(4.dp)
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
                    changePercentage = 20.0 + i,
                ),
                null
            )
        },
        hasErrorFetching = true
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