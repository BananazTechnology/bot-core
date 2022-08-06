package tech.bananaz.utils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import tech.bananaz.enums.Ticker;
import static java.util.Objects.isNull;

import java.math.BigDecimal;

public class CryptoValueLookup {

	private final String APIKEY = "Y29pbnNidDIwMjIhCg==";
	private final String COINS_URL = "https://proxy.aar.dev/coins?apikey="+APIKEY;
	private UrlUtils uUtils = new UrlUtils();
	
	public CryptoValueLookup() {}
	
	public BigDecimal getPriceInUSD(Ticker t) throws Exception {
		JSONObject coinResponse = this.uUtils.getObjectRequest(COINS_URL + "&pair=" + t.getSymbol().toUpperCase() + "-USD", null);
		// Within the response body we will either have and array of tickers like:
		/// {
		///   "tickers": []
		/// }
		// Or the ticker itself if only one:
		/// {
		///   "from": "{ticker}",
		///   "to": "USD",
		///   "price": 1234.56,
		///   "exchange": "Kraken",
		/// }
		JSONArray tickers = (JSONArray) coinResponse.get("tickers");
		String priceOfTickerInUsd = coinResponse.getAsString("price");
		// If our object was returned in the tickers array we get the price from it
		if(!tickers.isEmpty() && isNull(priceOfTickerInUsd)) {
			JSONObject tickerFirstObj = (JSONObject) tickers.get(0);
			priceOfTickerInUsd = tickerFirstObj.getAsString("price");
		}
		// Return our money value as a decimal
		return BigDecimal.valueOf(Double.valueOf(priceOfTickerInUsd));
	}
	
}
