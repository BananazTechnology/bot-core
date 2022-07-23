package tech.bananaz.utils;

import java.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.*;
import net.minidev.json.*;

public class OpenseaUtils {
	
	private static final String OS_BASE					   = "https://api.opensea.io/api/v1/";
	private static final String OPENSEA_EVENTS_URL   	   = OS_BASE+"events?event_type=%s&only_opensea=false&limit=%s&%s";
	private static final String COLLECTION_SLUG_QUERY 	   = "collection_slug=";
	private static final String CONTRACT_ADDRESS_QUERY 	   = "asset_contract_address=";
	private static final String ACCOUNT_ADDRESS_QUERY 	   = "account_address=";
	private static final String APIKEYHEAD 		   		   = "x-api-key";
	private static final int	POLL_SIZE 				   = 25;
	private static final String NULL_ADDRESS        	   = "0x0000000000000000000000000000000000000000";
	private RestTemplate restTemplate 			   		   = new RestTemplate();
	private StringUtils sUtils  				   		   = new StringUtils();
	private JsonUtils jsonUtils 						   = new JsonUtils();
	private String apiKey;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenseaUtils.class);
	
	private enum EventType {
		LISTINGS("created"),
		SALES("successful"),
		TRANSFER("transfer");
		
		private String eventType;

		EventType(String string) {
			this.eventType = string;
		}
		
		public String toString() {
			return this.eventType;
		}
		
	}
	
	public OpenseaUtils(String apiKey) {
		if(!apiKey.equals("")) {
			this.apiKey = apiKey;
		} else {
			throw new RuntimeException("Opensea API key needed for library calls!");
		}
	}
	
	public JSONObject getEventsSalesAddress(String address) throws Exception {
		return getCollectionEvents(address, EventType.SALES);
	}
	
	public JSONObject getEventsSalesSlug(String slug) throws Exception {
		return getCollectionEventsWithSlug(slug, EventType.SALES);
	}
	
	public JSONObject getEventsListingsAddress(String address) throws Exception {
		return getCollectionEvents(address, EventType.LISTINGS);
	}
	
	public JSONObject getEventsListingsSlug(String slug) throws Exception {
		return getCollectionEventsWithSlug(slug, EventType.LISTINGS);
	}
	
	/**
	 * Lookup collection events by collection slug SPECIFIC to OpenSea
	 * @param contract
	 * @return
	 */
	private JSONObject getCollectionEventsWithSlug(String slug, EventType eventType) throws Exception {
		String buildUrl = String.format(
				OPENSEA_EVENTS_URL, 
				eventType,
				POLL_SIZE,
				COLLECTION_SLUG_QUERY + slug);
		
		// Create header for validating
		HttpHeaders headers = new HttpHeaders();
		headers.set(APIKEYHEAD, this.apiKey);
		// Wrap headers in request body
		HttpEntity<String> wrappedRequest = new HttpEntity<>(headers);
		
		return getAllRequest(buildUrl, wrappedRequest);
		
	}
	
	/**
	 * Lookup collection events by the contract address
	 * @param contract
	 * @return
	 */
	private JSONObject getCollectionEvents(String address, EventType eventType) throws Exception {
		String buildUrl = String.format(
				OPENSEA_EVENTS_URL, 
				eventType,
				POLL_SIZE,
				CONTRACT_ADDRESS_QUERY + address);
		
		// Create header for validating
		HttpHeaders headers = new HttpHeaders();
		headers.set(APIKEYHEAD, this.apiKey);
		// Wrap headers in request body
		HttpEntity<String> wrappedRequest = new HttpEntity<>(headers);
		
		return getAllRequest(buildUrl, wrappedRequest);
		
	}
	
	/**
	 * This is a helper method, in this method you can provide a String of the URL for 
	 * requesting and it will return a JSONObject of the response from OpenSea.
	 * 
	 * @param getURL Pass in a String of the API request URL.
	 * @return A json-smart object of the
	 * @throws HttpException 
	 * @throws InterruptedException
	 */
	private JSONObject getAllRequest(String getURL, HttpEntity<String> requestMetadata) throws Exception {
		// Variables for runtime
		URI createURI = sUtils.getURIFromString(getURL);
		ResponseEntity<String> result = null;
		JSONObject newResponse = new JSONObject();
		// Variables for timing
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		try {
			// Runs for events endpoint, this to to append api key
			if(requestMetadata != null) {
				// Create HTTP Call
				result = restTemplate.exchange(createURI, HttpMethod.GET, requestMetadata, String.class);
				// Parse String response for JSONObject
				newResponse = jsonUtils.stringToJsonObject(result.getBody());
				endTime = System.currentTimeMillis();
			} else {
				result = restTemplate.getForEntity(createURI, String.class);
				newResponse = jsonUtils.stringToJsonObject(result.getBody());
			}
		} catch (HttpClientErrorException e) {
			LOGGER.error(String.format("Failed HTTP GET: [%s] %s - %s", e.getRawStatusCode(), e.getStatusText(), e.getResponseHeaders().toSingleValueMap()));
			throw new Exception(String.format("Failed HTTP GET: [%s] %s - %s", e.getRawStatusCode(), e.getStatusText(), e.getResponseHeaders().toSingleValueMap()));
		}
		LOGGER.debug(String.format("GET request took %sms", Long.valueOf(endTime-startTime).toString()));
		return newResponse;
	}
}
