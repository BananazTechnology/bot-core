package tech.bananaz.utils;

import net.minidev.json.JSONObject;

public class KeyUtils {

	private final String APIKEY = "YnRrZXlzMjAyMgo=";
	private final String KEY_URL = "https://proxy.aar.dev/api/keys?apikey="+APIKEY;
	private UrlUtils uUtils = new UrlUtils();
	
	public KeyUtils() {}
	
	public String getKey() throws Exception {
		JSONObject keyResponse = this.uUtils.getObjectRequest(KEY_URL, null);
		return keyResponse.getAsString("key");
	}
	
}
