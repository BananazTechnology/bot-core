package tech.bananaz.utils;

import net.minidev.json.JSONObject;

public class KeyUtils {

	private final String APIKEY = "J097-akxaPCie-M7QWEdGU8bn-yld-Vx8i4-S5LRth-B3";
	private final String KEY_URL = "https://proxy.bananaz.tech/api/keys?apikey="+APIKEY;
	private UrlUtils uUtils = new UrlUtils();
	
	public KeyUtils() {}
	
	public String getKey() throws Exception {
		JSONObject keyResponse = this.uUtils.getObjectRequest(KEY_URL, null);
		return keyResponse.getAsString("key");
	}
	
}
