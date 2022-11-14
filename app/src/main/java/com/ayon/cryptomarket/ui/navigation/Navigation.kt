package com.ayon.cryptomarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayon.cryptomarket.ui.market.MarketRoute

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MARKET) {
        composable(Routes.MARKET) { MarketRoute(navController) }
    }
}

object Routes {
    const val MARKET = "market"
}