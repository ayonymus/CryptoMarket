package com.ayon.cryptomarket.data.token.cmc.remote

import com.ayon.cryptomarket.domain.Symbol
import com.ayon.cryptomarket.domain.TokenDetails
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinMarketCapService {

    /*
        Note
        This is wrong:
        - api key should not be under source control, it should be injected to the build
        - an OkHTTP interceptor could add the keys automatically to all queries of this backend
        However, I wanted to ship a working app as quick as possible, so here we are.
     */

    @Headers("X-CMC_PRO_API_KEY: bc6ef990-8063-4328-997f-6884cc917b01")
    @GET("v2/cryptocurrency/info")
    suspend fun getTokenInfo(@Query("symbol", encoded = true) symbols: String): Response<CMCResponse>
}

data class CMCResponse(
    val data: Map<Symbol, List<CMCToken>>
)

data class CMCToken(
    val symbol: String,
    val description: String,
    val name: String,
    val slug: String,
    val logo: String,
    val urls: CMCUrls?
)

data class CMCUrls (

    @SerializedName("website") val website: List<String> = listOf(),
    @SerializedName("reddit") val reddit: List<String> = listOf(),
    @SerializedName("technical_doc") val technicalDoc: List<String> = listOf(),
    @SerializedName("source_code") val sourceCode: List<String> = listOf()

)

fun CMCToken.toTokenDetails() = TokenDetails(
    symbol = symbol,
    name = name,
    logo = logo,
)
