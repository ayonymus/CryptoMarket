package com.ayon.cryptomarket.data.market.bitfinex

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type

interface BitfinexApi {

    @GET("v2/tickers")
    suspend fun getTickers(@Query("symbols", encoded = true) marketSymbolList: String): Response<List<BitfinexTicker>>

}

typealias TradingSymbol = String

data class BitfinexTicker(
    val symbol: TradingSymbol,
    val last_price: Double,
    val daily_change_relative: Double,
)

class BitfinexTickerDeserializer: JsonDeserializer<BitfinexTicker> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BitfinexTicker {
        val arr = json.asJsonArray

        return BitfinexTicker(
            symbol = arr.get(0).asString,
            daily_change_relative = arr.get(9).asDouble,
            last_price = arr.get(10).asDouble
        )
    }
}