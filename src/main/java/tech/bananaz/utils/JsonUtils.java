package tech.bananaz.utils;

import java.util.Map.Entry;
import static java.util.Objects.nonNull;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class JsonUtils {

	/**
	 * Gets a value from a JSONObject
	 * 
	 * @param jsonObject
	 *            The JSONObject to get the value from
	 * @param key
	 *            The key to get the value from
	 * @return The value of the key
	 */
	public JSONObject stringToJsonObject(String data) {
		JSONObject jsonData = null;
		try {
			JSONParser jsonParser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			Object bufferObject = jsonParser.parse(data);
			jsonData = (JSONObject) bufferObject;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonData;
	}

	public JSONArray stringToJsonArray(String data) {
		JSONArray jsonData = null;
		try {
			JSONParser jsonParser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			Object bufferObject = jsonParser.parse(data);
			jsonData = (JSONArray) bufferObject;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonData;
	}
	
	/**
	 * Parse a JSONObject endlessly for a key within the JSON.
	 * The matching is only the java contains function an they JSON key 
	 *     AND your provided key are to lower case.
	 * 
	 * @param m
	 * @param key
	 * @return
	 */
	public static String parseObjectForKey(JSONObject m, String key) {
		String res = null;
		String objToLc = m.toJSONString().toLowerCase();
		String keyLc = key.toLowerCase();
		if(objToLc.contains(keyLc)) {
			for(Entry<String, Object> kv : m.entrySet()) {
				String entryKeyLc = kv.getKey().toLowerCase();
				Object valueType = kv.getValue();
				if(entryKeyLc.contains(keyLc)) {
					if(valueType instanceof String ||
						valueType instanceof Integer || 
						valueType instanceof Boolean || 
						valueType instanceof Double) { 
						String valueToString = String.valueOf(valueType);
						return valueToString;
					}
					if(valueType instanceof JSONArray) {
						JSONArray value = (JSONArray) kv.getValue();
						String out = parseArrayForKey(value, keyLc);
						if(nonNull(out)) return out;
					}
				}
			}
		}
		return res;
	}
	
	/**
	 * This parse JSON array function is simple.
	 * The allowed items inside an array is only primitives and Objects,
	 * Since this function only supports finding k/v pairs,
	 * We only process arrays that have objects in them... this function calls parseObjectForKey.
	 * 
	 * Searching is case insensitive and partial with the String contains function.
	 * 
	 * @param m
	 * @param key
	 * @return
	 */
	public static String parseArrayForKey(JSONArray m, String key) {
		String res = null;
		String objToLc = m.toJSONString().toLowerCase();
		String keyLc = key.toLowerCase();
		if(objToLc.contains(keyLc)) {
			for(int i = 0; i < m.size(); i++) {
				Object nestedValue = m.get(i);
				if(nestedValue instanceof JSONObject) {
					JSONObject nestedObj = (JSONObject) nestedValue;
					String out = parseObjectForKey(nestedObj, keyLc);
					if(nonNull(out)) return out;
				}
			}
		}
		return res;
	}
	
	public static String findNFTMetadataAttribute(JSONObject m, String key) {
		String res = null;
		String objToLc = m.toJSONString().toLowerCase();
		String keyLc = key.toLowerCase();
		if(objToLc.contains(keyLc)) {
			for(Entry<String, Object> kv : m.entrySet()) {
				Object valueType = kv.getValue();
				if(valueType instanceof JSONArray) {
					JSONArray value = (JSONArray) kv.getValue();
					String out = parseNFTAttributesArray(value, keyLc);
					if(nonNull(out)) return out;
				}
			}
		}
		return res;
	}
	
	private static String parseNFTAttributesArray(JSONArray m, String key) {
		String res = null;
		String objToLc = m.toJSONString().toLowerCase();
		String keyLc = key.toLowerCase();
		if(objToLc.contains(keyLc)) {
			for(int i = 0; i < m.size(); i++) {
				Object nestedValue = m.get(i);
				if(nestedValue instanceof JSONObject) {
					JSONObject nestedObj = (JSONObject) nestedValue;
					String out = parseNFTAttributeObject(nestedObj, keyLc);
					if(nonNull(out)) return out;
				}
			}
		}
		return res;
	}
	
	private static String parseNFTAttributeObject(JSONObject m, String key) {
		String res = null;
		String objToLc = m.toJSONString().toLowerCase();
		String keyLc = key.toLowerCase();
		final String[] value_names = {"value"};
		if(objToLc.contains(keyLc)) {
			for(Entry<String, Object> kv : m.entrySet()) {
				String entryKeyLc = kv.getKey().toLowerCase();
				Object valueType = kv.getValue();
				// Parse key
				for( int i=0; i<value_names.length; i++) {
				    String traitKey = value_names[i].toLowerCase();
				    if(entryKeyLc.contains(traitKey)) {
				    	if(valueType instanceof String ||
							valueType instanceof Integer ||
							valueType instanceof Long    ||
							valueType instanceof Boolean || 
							valueType instanceof Double) { 
							String valueToString = String.valueOf(valueType);
							return valueToString;
						}
				    }
				}
			}
		}
		return res;
	}
	
	
}
