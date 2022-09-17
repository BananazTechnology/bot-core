package tech.bananaz.utils;

import static java.util.Objects.nonNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import tech.bananaz.enums.RarityEngine;
import tech.bananaz.models.Rarity;
import static tech.bananaz.utils.JsonUtils.findNFTMetadataAttribute;

public class RarityUtils {

	// BT Infrastructure
	private final String BT_APIKEY 				 = "zEvbetbhGaYfG6TtZCgvapS5BM3Hl3L2";
	private final String BT_APIKEY_URI_PARAM     = "?apikey="+BT_APIKEY;
	private static final String GEISHA_API_URL   = "https://proxy.bananaz.tech/api/raritys/geisha/";
	// Public domain
	private static final String TRAIT_SNIPER_URL = "https://api.traitsniper.com/api/projects/%s/nfts?token_id=%s&trait_count=true&trait_norm=true";
	private static final String RARITY_STRING    = "rarity";
	private static UrlUtils urlUtils   			 = new UrlUtils();
	
	private Rarity getGeishaRarity(String tokenId) {
		Rarity rarityCalc = new Rarity();
		try {
			JSONObject response = urlUtils.getObjectRequest(GEISHA_API_URL + tokenId + BT_APIKEY_URI_PARAM, null);
			rarityCalc.setRarityEngine( RarityEngine.RARITY_TOOLS );
			rarityCalc.setRarity( response.getAsString("rarity") );
			rarityCalc.setRarityUrl( String.format(RarityEngine.RARITY_TOOLS.getUrl(), "geisha", tokenId) );
		} catch (Exception e) {}
		return rarityCalc;
	}
	
	private Rarity getTraitSniperRarity(String lookupId, String tokenId) {
		Rarity rarityCalc = new Rarity();
		try {
			String buildUrl 		= String.format(TRAIT_SNIPER_URL, lookupId, tokenId);
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
	


	private Rarity getMetadataRarity(String metadataUrl) {
		Rarity rarityCalc = new Rarity();
		try {
			JSONObject response = urlUtils.getObjectRequest(metadataUrl, null);
			rarityCalc.setRarity( findNFTMetadataAttribute(response, RARITY_STRING) );
			rarityCalc.setRarityEngine( RarityEngine.METADATA );
		} catch (Exception e) {}
		return rarityCalc;
	}
	
	@SuppressWarnings("incomplete-switch")
	public Rarity getRarity(String contractAddress, String slug, String tokenId, String metadata, RarityEngine re) {
		Rarity rarityCalc = new Rarity();
		// Special customers are here in the IF statements
		if(nonNull(slug))
			if(slug.equalsIgnoreCase("supergeisha")) return getGeishaRarity(tokenId);
		
		if(nonNull(re)) {
			switch(re) {
				//
				// Metadata
				case METADATA:
					// Do metadata parsing
					return getMetadataRarity(metadata);
				//
				// Trait Sniper
				case TRAIT_SNIPER:
					// If auto rarity and rairty has not just been set above
					return getTraitSniperRarity(contractAddress, tokenId);
			}
		}
		return rarityCalc;
	}

}
