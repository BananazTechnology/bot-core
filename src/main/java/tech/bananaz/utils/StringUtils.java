package tech.bananaz.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import tech.bananaz.enums.Ticker;
import tech.bananaz.models.Event;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import java.math.BigDecimal;

public class StringUtils {
	
	private static final DecimalFormat dFormat  = new DecimalFormat("####,###,###.00");
	private static final int MAXHASHTAGS = 4;
	private final static String BRAIL_BLANK = "⠀";
	private final static String NEWLINE = "\n";

	/**
	 * This is a helper method, in this method you can supply a valid 
	 * String of a web address and receive a Java URI for other methods.
	 * 
	 * @param string A String containing a web address
	 * @return A java.net URI for all your web needs
	 * @since 1.0
	 */
	public URI getURIFromString(String string) {
		URI newURI = null;
		try {
			newURI = new URI(string);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return newURI;
	}
	
	public String addressSimple(String address, String bidderENSLookup) throws NullPointerException {
		String returnString;
		returnString = (address.equals(bidderENSLookup)) ? address.substring(0, 8) : bidderENSLookup;
		return returnString;
	}
	
	/**
	 * For twitter
	 * @param headerList
	 * @return
	 */
	public String getRandomHeader(List<String> headerList) {
		Collections.shuffle(headerList);
		return headerList.get(0);
	}
	
	/**
	 * For twitter
	 * @param hashtagList
	 * @return
	 */
	public String getRandomHashtags(List<String> hashtagList) {
		// init blank string for all hashtags
		String hashtagBuffer = "";
		// shuffle hashtag list
		Collections.shuffle(hashtagList);
		for(int i = 0; (i < MAXHASHTAGS) && (i < hashtagList.size()); i++) {
			// adds new hashtag to the hashtag buffer
			hashtagBuffer+=hashtagList.get(i);
			// dontn forget the space, better ui look
			hashtagBuffer+=" ";
		}
		return hashtagBuffer;
	}
	
	/**
	 * Compares two strings using the .equalsIgnoreCase function
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean equalsIgnoreCase(String a, String b) {
		return a.equalsIgnoreCase(b);
	}
	
	/**
	 * Compares two strings using the .equals function
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(String a, String b) {
		return a.equals(b);
	}
	
	/**
	 * Compares two strings using the .equals function and uses the negation to return opposite
	 * @param a
	 * @param b
	 * @return false by default
	 */
	public static boolean nonEquals(Object a, Object b) {
		return !(String.valueOf(a).equalsIgnoreCase(String.valueOf(b)));
	}
	
	/**
	 * Main string price formatting for DiscordConfig
	 * @param priceInCrypto
	 * @param cryptoPaymentType
	 * @param priceInUsd
	 * @return
	 */
	public String pricelineFormat(BigDecimal priceInCrypto, Ticker cryptoPaymentType, BigDecimal priceInUsd) {
		String cryptoValue = (nonNull(priceInCrypto)) ? String.format("%s", priceInCrypto) : "NULL";
		String paymentType = (nonNull(cryptoPaymentType)) ? cryptoPaymentType.getSymbol() : "";
		String usdValue    = (nonNull(priceInUsd)) ? String.format("($%s)", dFormat.format(priceInUsd.doubleValue())) : "";
		return String.format("%s%s %s", cryptoValue, paymentType, usdValue);
	}
	
	/**
	 * Fail-safe method to format the display name for an NFT
	 * @param nftName
	 * @param collectionName
	 * @param tokenId
	 * @return
	 * @throws NullPointerException Specify this to handle
	 */
	public String buildNftDisplayName(String nftName, String collectionName, String tokenId) throws NullPointerException {
		return (nonNull(nftName)) ? nftName : collectionName + " #" + tokenId;
	}
	
	/**
	 * Looks for a username K/V pair in the data JSONObject or return chars 0-8 of the address
	 * @param data
	 * @param address
	 * @return
	 * @throws NullPointerException Specify this to handle
	 */
	public String tryUsernameOrFormatAddress(JSONObject data, String address) throws NullPointerException {
		try {
			String response = data.getAsString("username");
			if(nonNull(response)) {
				if(!response.isBlank() && 
				   !response.isEmpty() && 
				   !response.equalsIgnoreCase("null") && 
				   !response.equalsIgnoreCase("nil")) {
					return response;
				}
			}
		} catch (Exception e) { }
		return simplifyEthAddress(address);
	}
	
	/**
	 * Return chars 0-8 of a string
	 * @param address
	 * @return
	 */
	public String simplifyEthAddress(String address) {
		return address.substring(0, 8);
	}
	
	/**
	 * Returns a save image for DiscordConfig, if imageUrl is png or blank or empty this function returns collectionImageUrl
	 * @param imageUrl
	 * @param collectionImageUrl
	 * @return
	 */
	public String chooseImageUrl(String imageUrl, String collectionImageUrl) {
		if(isNull(imageUrl)) return collectionImageUrl;
		if(nonNull(imageUrl)) {
			if(imageUrl.contains(".svg") || imageUrl.isBlank() || imageUrl.isEmpty()) {
				return collectionImageUrl;
			}
		}
		return imageUrl;
	}
	
	/**
	 * Calls lower case on the input string then capitalizes the first letter
	 * @param str
	 * @return
	 */
	public static String capitalizeFirstLetter(String str) {
	    if(str == null || str.isEmpty()) {
	        return str;
	    }
	    String loc = str.toLowerCase();
	    return loc.substring(0, 1).toUpperCase() + loc.substring(1);
	}
	
	/**
	 * Formats data from an Event into a string template.
	 * The way to represent data from the Event in the template is described in a 3 different parts
	 * 1.) Defining a new function which is by using colons in the format :x: where x is described in #2
	 * 2.) A non-case-specific variable from Event, no spaces
	 * 
	 * Some specific things to know:
	 * - use each value only ONCE in the template
	 * - newlines must be entered into string as \n
	 * - priceInCrypto renders with the crypto symbol appended directly at the end like 100Ξ
	 * - priceInUsd displays as USD format rounded to the penny with commas,
	 *     when not null USD is rendered as ($1,234.56) with parentheses and dollar sign
	 * 
	 * @param template
	 * @param e
	 * @return string formatted
	 */
	@SuppressWarnings("unchecked")
	public static String stringTemplateFormatEvent(String template, Event e) {
		// Dont use template, use this
		final String templateCleaned = template.strip().trim();
		// Temp variable to store changes while parsing Event
		String buffer = templateCleaned;
		// Event to Map for simple k/v parsing
		ObjectMapper objMapper = new ObjectMapper();
		Map < String, Object > eventMap = objMapper.convertValue(e, Map.class);
		// Loop through keys of event
		for(Entry<String, Object> eventKeyValues : eventMap.entrySet()) {
			// The best way to compare is to always use same case
			// we take lower-case of buffer and lower-case our key and surround with our colons
			// Example: 
			// - keyLC = abcd
			// - keyTemplateLC = :abcd:
			final String keyLC = eventKeyValues.getKey().toLowerCase();
			final String keyTemplateLC = String.format(":%s:", keyLC);
			final String bufferLC = buffer.toLowerCase();
			// If lower-case buffer contains our value :abcd:
			if(bufferLC.contains(keyTemplateLC)) {
				// Get the position in the string of our key
				int keyStart = bufferLC.indexOf(keyTemplateLC);
				int keyEnd = keyStart+keyTemplateLC.length();
				// Select out the proper capitalization used by selecting exact position
				// By doing all our previous login in lower-case we can accept functions as case insensitive
				// By selecting this value as exact case provided we can run a simpler replace method
				String keyInTemplate = buffer.substring(keyStart, keyEnd);
				// We use the if statements below to perform special formatting on currencies
				// Price In Crypto
				if(keyLC.equalsIgnoreCase("priceInCrypto")) 
					buffer = buffer.replace(keyInTemplate, String.format("%s%s", e.getPriceInCrypto(), e.getCryptoType().getSymbol()));
				// Price In USD
				else if(keyLC.equalsIgnoreCase("priceInUsd")) {
					if(nonNull(e.getPriceInUsd()))
						buffer = buffer.replace(keyInTemplate, String.format("($%s)", dFormat.format(e.getPriceInUsd().doubleValue())));
					else 
						buffer = buffer.replace(keyInTemplate, "");
				}
				// This last else formats all other values
				else
					buffer = buffer.replace(keyInTemplate, String.format("%s", eventKeyValues.getValue()));
			}
		}
		// Prep outbound response default is null
		String outbound = null;
		// Append our special ending values
		if(!templateCleaned.equalsIgnoreCase(buffer)) outbound = (buffer + BRAIL_BLANK + NEWLINE);
		return outbound;
	}
}
