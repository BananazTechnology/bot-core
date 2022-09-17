package tech.bananaz.utils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import tech.bananaz.enums.Ticker;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.math.BigDecimal;

public class CryptoValueLookup {

	// BT Infrastructure
	private final String BT_APIKEY    = "Y29pbnNidDIwMjIhCg==";
	private final String BT_COINS_URL = "https://proxy.bananaz.tech/api/coins?apikey="+BT_APIKEY+"&pair=";
	// Public domain
	private final String COINSTATS_URL = "https://api.coinstats.app/public/v1/tickers?exchange=kraken&pair=";
	private UrlUtils uUtils = new UrlUtils();
	
	public CryptoValueLookup() {}
	
	public BigDecimal getPriceInUSD(Ticker t) throws Exception {
		String lookupTicker = (nonNull(t.getLookupValue())) ? t.getLookupValue().toUpperCase() : t.getTicker().toUpperCase();
		BigDecimal response = null;
		
		if(isNull(response)) {
			try {
				JSONObject coinResponse = this.uUtils.getObjectRequest(BT_COINS_URL + lookupTicker + "-USD", null);
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
				if(nonNull(tickers) && isNull(priceOfTickerInUsd)) {
					if(tickers.size() > 0 ) {
						JSONObject tickerFirstObj = (JSONObject) tickers.get(0);
						priceOfTickerInUsd = tickerFirstObj.getAsString("price");
					}
				}
				if(nonNull(priceOfTickerInUsd))
					response = BigDecimal.valueOf(Double.valueOf(priceOfTickerInUsd));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(isNull(response)) {
			try {
				JSONObject coinResponse = this.uUtils.getObjectRequest(COINSTATS_URL + lookupTicker + "-USD", null);
				// Within the response body we will either have and array of tickers like:
				/// {
				///   "tickers": [
				///     {
				///       "from": "{ticker}",
				///       "to": "USD",
				///       "price": 1234.56,
				///       "exchange": "Kraken",
				///     }
				///   ]
				/// }
				// Or the ticker itself if only one:
				/// 
				JSONArray tickers = (JSONArray) coinResponse.get("tickers");
				String price = null;
				if(nonNull(tickers)) {
					if(tickers.size() > 0) {
						JSONObject tickerFirstObj = (JSONObject) tickers.get(0);
						price = tickerFirstObj.getAsString("price");
					}
				}
				
				if(nonNull(price))
					response = BigDecimal.valueOf(Double.valueOf(price));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// Return our money value as a decimal
		return response;
	}
	
}
