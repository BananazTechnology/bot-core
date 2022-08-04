package tech.bananaz.utils;

import static java.util.Objects.nonNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import tech.bananaz.enums.RarityEngine;
import tech.bananaz.models.Rarity;

public class RarityUtils {
	
	private static final String DF_API_URL      = "http://proxy.aaronrenner.com/api/rarity/deadfellaz/";
	private static final String GEISHA_API_URL  = "http://proxy.aaronrenner.com/api/rarity/geisha/";
	private static final String AUTO_RARITY_URL = "https://api.traitsniper.com/api/projects/%s/nfts?token_id=%s&trait_count=true&trait_norm=true";
	private static UrlUtils urlUtils   			= new UrlUtils();
	
	private Rarity getDeadfellazRarity(String tokenId) {
		Rarity rarityCalc = new Rarity();
		try {
			JSONObject response = urlUtils.getObjectRequest(DF_API_URL + tokenId, null);
			rarityCalc.setRarity( response.getAsString("rarity") );
			rarityCalc.setRarityEngine( RarityEngine.RARITY_TOOLS );
			rarityCalc.setRarityUrl( String.format(RarityEngine.RARITY_TOOLS.getUrl(), "deadfellaz", tokenId) );
		} catch (Exception e) {}
		return rarityCalc;
	}
	
	private Rarity getGeishaRarity(String tokenId) {
		Rarity rarityCalc = new Rarity();
		try {
			JSONObject response = urlUtils.getObjectRequest(GEISHA_API_URL + tokenId, null);
			rarityCalc.setRarityEngine( RarityEngine.RARITY_TOOLS );
			rarityCalc.setRarity( response.getAsString("rarity") );
			rarityCalc.setRarityUrl( String.format(RarityEngine.RARITY_TOOLS.getUrl(), "geisha", tokenId) );
		} catch (Exception e) {}
		return rarityCalc;
	}
	
	private Rarity getAutoRarity(String lookupId, String tokenId) {
		Rarity rarityCalc = new Rarity();
		try {
			String buildUrl 		= String.format(AUTO_RARITY_URL, lookupId, tokenId);
			// Add User-Agent for legality
			HttpHeaders headers 	= new HttpHeaders();
			headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.29.0");
			HttpEntity<String> prop = new HttpEntity<>(headers);
			JSONObject response 	= urlUtils.getObjectRequest(buildUrl, prop);
			JSONArray nfts 			= (JSONArray) response.get("nfts");
			JSONObject firstItem 	= (JSONObject) nfts.get(0);
			rarityCalc.setRarity( firstItem.getAsString("rarity_rank") );
			rarityCalc.setRarityEngine( RarityEngine.TRAIT_SNIPER );
			rarityCalc.setRarityUrl( String.format(RarityEngine.TRAIT_SNIPER.getUrl(), lookupId, tokenId) );
		} catch (Exception e) {}
		return rarityCalc;
	}
	
	public Rarity getRarity(String contractAddress, String slug, String tokenId, boolean autoRarity) {
		Rarity rarityCalc = new Rarity();
		// Ensure all variables are set to run rarity checks INCLUDING a null rarity so that we don't make extra HTTP requests
		if(nonNull(tokenId) && nonNull(slug)) {
			// Deadfellaz
			if(slug.equalsIgnoreCase("deadfellaz"))  rarityCalc = getDeadfellazRarity(tokenId);
			// Super Geisha
			if(slug.equalsIgnoreCase("supergeisha")) rarityCalc = getGeishaRarity(tokenId);
			// If auto rarity and rairty has not just been set above
			if(nonNull(contractAddress) && autoRarity) {
				rarityCalc = getAutoRarity(contractAddress, tokenId);
			}
		}
		return rarityCalc;
	}
	
	

}
