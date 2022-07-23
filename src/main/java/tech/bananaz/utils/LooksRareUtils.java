package tech.bananaz.utils;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import net.minidev.json.JSONObject;

public class LooksRareUtils {

	// Base URL is the query endpoint for the subgraph we need to query
	private static String BASE_URL       = "https://api.looksrare.org/api/v1/events?collection=%s&type=%s";
	private RestTemplate restTemplate    = new RestTemplate();
	private StringUtils sUtils         	 = new StringUtils();
	private JsonUtils jUtils 	   	   	 = new JsonUtils();
	private static final Logger LOGGER 	 = LoggerFactory.getLogger(LooksRareUtils.class);
	
	private enum EventType {
		LISTINGS("LIST"),
		SALES("SALE");
		
		private String eventType;

		EventType(String string) {
			this.eventType = string;
		}
		
		public String toString() {
			return this.eventType;
		}
		
	}
	
	public JSONObject getEventsSalesAddress(String address) throws Exception {
		return getEvents(address, EventType.SALES);
	}
	
	public JSONObject getEventsListingsAddress(String address) throws Exception {
		return getEvents(address, EventType.LISTINGS);
	}
	
	/**
	 * Lookup collection events by the contract address
	 * @param contract
	 * @return
	 */
	private JSONObject getEvents(String contract, EventType eventType) throws Exception {
		String buildUrl = String.format(
				BASE_URL, 
				contract, 
				eventType);
		return getAllRequest(buildUrl);
	}
	
	/**
	 * This is a helper method, in this method you can provide a String of the URL for 
	 * requesting and it will return a JSONObject of the response from LooksRare
	 * 
	 * @param getURL Pass in a String of the API request URL.
	 * @return A json-smart object of the
	 * @throws HttpException 
	 * @throws InterruptedException
	 */
	private JSONObject getAllRequest(String getURL) throws Exception {
		// Variables for runtime
		URI createURI = sUtils.getURIFromString(getURL);
		ResponseEntity<String> result = null;
		JSONObject newResponse = new JSONObject();
		// Variables for timing
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		try {
			result = restTemplate.getForEntity(createURI, String.class);
			newResponse = jUtils.stringToJsonObject(result.getBody());
		} catch (HttpClientErrorException e) {
			LOGGER.error(String.format("Failed HTTP GET: [%s] %s - %s", e.getRawStatusCode(), e.getStatusText(), e.getResponseHeaders().toSingleValueMap()));
			throw new Exception(String.format("Failed HTTP GET: [%s] %s - %s", e.getRawStatusCode(), e.getStatusText(), e.getResponseHeaders().toSingleValueMap()));
		}
		LOGGER.debug(String.format("GET request took %sms", Long.valueOf(endTime-startTime).toString()));
		return newResponse;
	}
}
