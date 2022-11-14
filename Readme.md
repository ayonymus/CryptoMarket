# CryptoMarkets

## Features
This app is polling a list of crypto trade pairs from an end point and polls every 5 seconds.
Displays basic information about the tokens.
User can filter the list of tokens.

## Tech used
I was following **CleanArchitecture** principles so that the code isn't tightly coupled.
On the view layer I have used **MVI** for producing predictable view states.

I have used the following libraries:
* `Compose` for defining the UI
* `Coroutintes` and `Flow` for asynchronous data
* `Koin` for dependency injection
* `Room` for storing info on tokens
* `Retrofit` for network access

I have also added unit tests for a couple of critical paths, but not everything is covered.

## Implementation challenges

The biggest challenge was Bitfinex endpoints. I have created a Postman collection for probing the different calls. 
I haven't found an endpoint that would serve simple data on Tokens, e.g. assets that I could display and names. 
Designs provided had this kind of data, so I decided to dig in.

### Bitfinex endpoint issues
> You will use the Bitfinex API (https://docs.bitfinex.com/reference#rest-public-tickers )

I have found the following issues with the end point:
* LUNA still seem to refer to the (now defunct) classic Terra, which now has new symbol: LUNC
* Endpoint doesn't seem to return 24 hour relative price change properly, so I disabled in the ui
* Endpoint documentation is out of date. Data fields have to be guessed
* Bitfinex APIs lack a "Token details" endpoint (symbol, token name, token url, description, etc.)

### CoinMarketCap for Token Details
I have turned to Coingecko and CoinMarketCap. CoinGecko is free, but the returned data is excessive.

I decided to take **basic token information** from **CoinMarketCap**, even though they require an api key. 
Number of queries are limited. Therefore, TokenDetails are stored in a local Room database in order to limit CMC queries


## Improvements in the future

Currently there are no UI tests. Some compionents are not covered by regular unit tests.

Bitfinex has a websocket, which could be better suited for getting the most up to date results than polling every 5 sends.
Therefore, market data access is designed in a way so that only the `FlowingMarketRepository` has to be reimplemented

Currently the ticker pairs are hard coded.
However, the `TradingPairPreferences` interface and the `TickerPair` data class makes it possible to change this to a user modifiable list.

The `BitfinexTradeSymbolSource` implementation is also static. This could be connected to the Bitfinex configs endpoint, and retrieve dynamic pairs

it would be nice to add a `preferences` screen so that user can change/select Tickers.

Modularization would also be nice.

Token Repository should be refershed every now and then.

Styling could be massively improved. Both visual code. 

Setting up ktlint or Detekt would improve code formatting issues.
