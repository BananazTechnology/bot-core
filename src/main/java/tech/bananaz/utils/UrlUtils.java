package tech.bananaz.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import net.minidev.json.JSONObject;

public class UrlUtils {

	private RestTemplate restTemplate	= new RestTemplate();
	private StringUtils sUtils			= new StringUtils();
	private JsonUtils jsonUtils			= new JsonUtils();
	private static final Logger LOGGER  = LoggerFactory.getLogger(UrlUtils.class);
	/**
	 * This is a helper method, in this method you can provide a String of the URL for 
	 * requesting and it will return a JSONObject of the response from Steam.
	 * 
	 * @param getURL Pass in a String of the API request URL.
	 * @return A json-smart object of the
	 * @throws HttpException 
	 * @throws InterruptedException
	 */
	public JSONObject getObjectRequest(String getURL, HttpEntity<String> requestMetadata) throws Exception {
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
	
	public static byte[] imageUrlToBytes(String url) {
		byte[] output = null;
		URL addy = null;
		try {
			InputStream is = null;
			try {
				addy = new URL(url);
				is = addy.openStream ();
				output = IOUtils.toByteArray(is);
			}
			catch (IOException e) {
				System.err.printf ("Failed while reading bytes from %s: %s", addy.toExternalForm(), e.getMessage());
				// Perform any other exception handling that's appropriate.
			}
			finally {
				if (is != null) is.close();
			}
		} catch(IOException ex) {
			System.err.printf ("Failed while reading bytes from %s: %s", addy.toExternalForm(), ex.getMessage());
			// Perform any other exception handling that's appropriate.
		}
		return output;
	}
}
