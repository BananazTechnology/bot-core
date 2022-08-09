package tech.bananaz.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import net.minidev.json.JSONObject;
import tech.bananaz.enums.Ticker;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import java.math.BigDecimal;

public class StringUtils {
	
	private static final DecimalFormat dFormat  = new DecimalFormat("####,###,###.00");
	private static final int MAXHASHTAGS = 4;

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
}
