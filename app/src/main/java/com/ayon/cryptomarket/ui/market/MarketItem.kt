package com.ayon.cryptomarket.ui.market

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.ayon.cryptomarket.domain.TokenDetails
import com.ayon.cryptomarket.domain.Trade
import com.ayon.cryptomarket.domain.TradeDetails
import com.ayon.cryptomarket.domain.TradingPair
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@Composable
fun MarketItem(tradeItem: Trade) {
    ElevatedCard(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(4.dp),
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            val (icon, tokenName, symbol, price, change) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(tradeItem.tokenDetails?.logo),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            )

            val tradingPair = tradeItem.tradeDetails.tradingPair
            Text(
                text = tradeItem.tokenDetails?.name ?: tradingPair.symbol1,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(tokenName) {
                        start.linkTo(icon.end)
                        top.linkTo(icon.top)
                }
            )

            Text(
                text = tradingPair.symbol1,
                color = Color.Gray,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(symbol) {
                        start.linkTo(icon.end)
                        bottom.linkTo(icon.bottom)
                    }
            )

            val tradePrice = String.format("%.2f", tradeItem.tradeDetails.price)
            Text(
                text = tradeItem.tradeDetails.tradingPair.symbol2 + tradePrice,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(price) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
            )

            /*
            // API doesn't seem to respond with proper data, disable field

            val daily = tradeItem.tradeDetails.changePercentage
            val dailyChangeText = if (daily > 0) {
                "+ "
            } else {
                ""
            } + String.format("%.2f", daily) + "%"

            Text(
                text = dailyChangeText,
                color = if (daily < 0) { Color.Red } else { Color.Green },
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(change) {
                        end.linkTo(parent.end)
                        bottom.linkTo(icon.bottom)
                    }
            )

            */
        }
    }
}

@Preview
@Composable
fun MarketItemPreview() {
    val tradeItem = Trade(
        tradeDetails = TradeDetails(
            tradingPair = TradingPair("CHSB", "USD"),
            price = 16_000.000,
            changePercentage =
            +60.00
        ),
        tokenDetails = TokenDetails("CHSB", "SwissBorg", "https://s2.coinmarketcap.com/static/img/coins/64x64/2499.png"),
    )
    MarketItem(tradeItem = tradeItem)
}

@Preview
@Composable
fun MarketPlaceholderItem() {
    ElevatedCard(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(4.dp),
    ) {
        val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shimmer(shimmerInstance)
        ) {

            val (icon, tokenName, symbol) = createRefs()

            Box(
                modifier = Modifier.size(64.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .background(Color.LightGray)
            )

            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .constrainAs(tokenName) {
                        start.linkTo(icon.end)
                        top.linkTo(icon.top)
                    }
                    .background(Color.LightGray)
            )

            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .constrainAs(symbol) {
                        start.linkTo(icon.end)
                        bottom.linkTo(icon.bottom)
                    }
                    .background(Color.LightGray)
            )
        }
    }
}
