package tech.bananaz.utils;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;
import java.math.BigDecimal;
import java.time.Instant;
import net.minidev.json.JSONObject;
import tech.bananaz.enums.EventType;
import tech.bananaz.enums.MarketPlace;
import tech.bananaz.enums.Ticker;
import tech.bananaz.models.Config;
import tech.bananaz.models.Event;
import tech.bananaz.models.Rarity;
import tech.bananaz.utils.CryptoConvertUtils.Unit;
import static tech.bananaz.utils.StringUtils.*;

public class ParsingUtils {
	
	// Private
	private static final String ETHERSCAN_URL    = "https://etherscan.io/address/";
	/* private static final String SOLSCAN_URL      = "https://solscan.io/address/"; */
	private static final String OPENSEA_URL 	 = "https://opensea.io/";
	private static ENSUtils ensUtils   		     = new ENSUtils();
	private static CryptoConvertUtils convert    = new CryptoConvertUtils();
	private static RarityUtils rUtils   		 = new RarityUtils();
	private static CryptoValueLookup valueLookup = new CryptoValueLookup();
	
	public Event buildLooksRareEvent(JSONObject looksRareEvent, Config c) throws Exception {
		// Start return object 
		Event e = new Event();
		
		// Grab sub-objects in message
		JSONObject token 	   = (JSONObject) looksRareEvent.get("token");
		JSONObject collection  = (JSONObject) looksRareEvent.get("collection");
		JSONObject order  	   = (JSONObject) looksRareEvent.get("order");
		String listingInWei    = order.getAsString("price");
		
		// Grab direct variables
		Long    id 			     = Long.valueOf(looksRareEvent.getAsString("id"));
		String  name			 = token.getAsString("name");
		String  tokenId		     = token.getAsString("tokenId");
		String  collectionName   = collection.getAsString("name");
		String  slug             = String.valueOf(collectionName.toLowerCase().replace(" ", ""));
		Instant createdDate	     = Instant.parse(looksRareEvent.getAsString("createdAt"));
		Instant startTime	     = Instant.ofEpochSecond(order.getAsNumber("startTime").longValue());
		Instant endTime	         = Instant.ofEpochSecond(order.getAsNumber("endTime").longValue());
		String  imageUrl      	 = token.getAsString("imageURI");
		String  permalink		 = String.format("https://looksrare.org/collections/%s/%s", c.getContractAddress(), tokenId);
		String  metadata		 = token.getAsString("tokenURI");
		
		// Listing
		// Get seller information in all cases
		EventType eventType 	 = EventType.LIST; // Listing by default
		String  sellerWalletAddy = looksRareEvent.getAsString("from");
		String  ensSeller        = null;
		try {
			ensSeller 		     = ensUtils.getENS(sellerWalletAddy);
		} catch (Exception ex) {}
		String sellerName	  	 = (nonNull(ensSeller)) ? ensSeller : simplifyEthAddress(sellerWalletAddy);
		String sellerUrl		 = String.format("%s%s", ETHERSCAN_URL, sellerWalletAddy);
		
		// Sale
		// If event is a sale this to field will contain buyer
		String buyerWalletAddy	 = looksRareEvent.getAsString("to");
		String ensBuyer          = null;
		String buyerName		 = null;
		String buyerUrl			 = null;
		if(nonNull(buyerWalletAddy)) {
			eventType			 = EventType.SALE; // Buyer was available 
			try {
				ensBuyer 		 = ensUtils.getENS(buyerWalletAddy);
			} catch (Exception ex) {}
			buyerName	  	     = (nonNull(ensBuyer)) ? ensSeller : simplifyEthAddress(buyerWalletAddy);
			buyerUrl		     = String.format("%s%s", ETHERSCAN_URL, buyerWalletAddy);
		}
		
		// Make calculations about price
		BigDecimal priceInCrypto = convert.convertToCrypto(listingInWei, Unit.ETH);
		Integer quantity 	 	 = Integer.valueOf(order.getAsString("amount"));
		
		// Rarity lookup
		Rarity r = rUtils.getRarity(c.getContractAddress(), slug, tokenId, metadata, c.getRarityEngine());
		
		// Name formatting for when null
		name = buildNftDisplayName(name, collectionName, tokenId);
		
		// Calculate USD
		Ticker ticker = Ticker.ETH;
		BigDecimal priceInUsd = null;
		try {
			BigDecimal ethInUSD = valueLookup.getPriceInUSD(ticker);
			priceInUsd = priceInCrypto.multiply(ethInUSD);
		} catch (Exception ex) {}
		
		e.setId(id);
		e.setContractAddress(c.getContractAddress());
		e.setName(name);
		e.setCreatedDate(createdDate);
		e.setStartTime(startTime);
		e.setEndTime(endTime);
		e.setTokenId(tokenId);
		e.setCollectionName(collectionName);
		e.setSlug(slug);
		e.setImageUrl(imageUrl);
		e.setLink(permalink);
		e.setQuantity(quantity);
		e.setSellerWalletAddy(sellerWalletAddy);
		e.setSellerName(sellerName);
		e.setSellerUrl(sellerUrl);
		e.setBuyerWalletAddy(buyerWalletAddy);
		e.setBuyerName(buyerName);
		e.setBuyerUrl(buyerUrl);
		e.setRarity(r.getRarity());
		e.setRarityEngine(r.getRarityEngine());
		e.setRarityUrl(r.getRarityUrl());
		e.setPriceInCrypto(priceInCrypto);
		e.setPriceInUsd(priceInUsd);
		e.setCryptoType(ticker);
		e.setEventType(eventType);
		e.setMarket(MarketPlace.LOOKSRARE);
		e.setConsumed(false);
		e.setConfigId(c.getId());
		e.setHash(MarketPlace.LOOKSRARE, c.getContractAddress(), sellerWalletAddy, priceInCrypto.toPlainString());
		e.setMetadataUrl(metadata);
		return e;
	}
	
	public Event buildOpenSeaEvent(JSONObject openSeaEvent, Config c) throws Exception {
		// Start return object 
		Event e = new Event();
		
		// Grab sub-objects in message 
		JSONObject asset 	    = (JSONObject) openSeaEvent.get("asset");
		JSONObject paymentToken = (JSONObject) openSeaEvent.get("payment_token");
		JSONObject sellerObj    = (JSONObject) openSeaEvent.get("seller");
		JSONObject sellerUser   = (JSONObject) sellerObj.get("user");
		
		// Grab direct variables
		Long id 				= Long.valueOf(openSeaEvent.getAsString("id"));
		Instant createdDate		= Instant.parse(openSeaEvent.getAsString("created_date") + "Z");
		Instant startTime		= null;
		long duration           = 0;
		Instant endTime		    = null;
		Integer quantity 	    = Integer.valueOf(openSeaEvent.getAsString("quantity"));
		String sellerWalletAddy = sellerObj.getAsString("address");
		String sellerName	  	= tryUsernameOrFormatAddress(sellerUser, sellerWalletAddy);
		EventType eventType 	= EventType.LIST; // Listing by default
		
		// Process time events, need this for certain sales
		try {
			startTime = Instant.parse(openSeaEvent.getAsString("listing_time") + "Z");
			duration = (nonNull(openSeaEvent.getAsString("duration"))) ? Long.valueOf(openSeaEvent.getAsString("duration")) : 2630000; /// Try to get duration or assume OpenSea default 1 month
			endTime = Instant.ofEpochSecond((startTime.getEpochSecond() + duration));
		} catch (Exception ex) {}
		
		// Sale parsing
		// If event is a sale this to field will contain buyer
		JSONObject buyerObj     = (JSONObject) openSeaEvent.get("winner_account");
		String buyerName	    = null;
		String buyerUrl		    = null;
		String buyerWalletAddy  = null;
		if(nonNull(buyerObj)) {
			JSONObject buyerUser = (JSONObject) buyerObj.get("user");
			eventType = EventType.SALE;
			buyerWalletAddy = buyerObj.getAsString("address");
			buyerName = tryUsernameOrFormatAddress(buyerUser, buyerWalletAddy);
		}
		
		// Get price info from body 
		int decimals = 0;
		String usdOfPayment = null;
		Ticker cryptoType = null;
		String sellerUrl = null;
		String priceInWei = (eventType.equals(EventType.LIST)) ? 
								openSeaEvent.getAsString("starting_price") : // If Listing
									openSeaEvent.getAsString("total_price"); // If Sale
		
		// Process ETH on OpenSea
		if(nonNull(paymentToken)) {
			cryptoType 	   = Ticker.fromString(paymentToken.getAsString("symbol"));
			usdOfPayment   = paymentToken.getAsString("usd_price");
			decimals       = Integer.valueOf(paymentToken.getAsString("decimals"));
			sellerUrl	   = String.format("%s%s", OPENSEA_URL, sellerWalletAddy);
			// Grab the buyer URL if this event is a sale
			if(eventType.equals(EventType.SALE)) 
				buyerUrl = String.format("%s%s", OPENSEA_URL, buyerWalletAddy);
		} else {
			// Process SOL on OpenSea
			if(c.getSolanaOnOpensea()) {
				cryptoType = Ticker.SOL;
				decimals   = CryptoConvertUtils.Unit.SOL.getDecimal();
				sellerUrl  = String.format("%s%s", OPENSEA_URL, sellerWalletAddy);
				// Grab the buyer URL if this event is a sale
				if(eventType.equals(EventType.SALE)) 
					buyerUrl = String.format("%s%s", OPENSEA_URL, buyerWalletAddy);
			}
			// Process POLY on OpenSea
			if(c.getPolygonOnOpensea()) {
				cryptoType = Ticker.ETH;
				decimals   = CryptoConvertUtils.Unit.ETH.getDecimal();
				sellerUrl  = String.format("%s%s", OPENSEA_URL, sellerWalletAddy);
				// Grab the buyer URL if this event is a sale
				if(eventType.equals(EventType.SALE)) 
					buyerUrl = String.format("%s%s", OPENSEA_URL, buyerWalletAddy);
			}
			if(isNull(cryptoType)) {
				priceInWei = null;
			}
		}

		// Support for OpenSea bundles,
		// Asset var will be empty and this asset_bundle will have data
		JSONObject assetBundleObj   = (JSONObject) openSeaEvent.get("asset_bundle");
		JSONObject assetInfo = (nonNull(asset)) ? asset : assetBundleObj;
			
		// Get important values from the assets
		String name 		  	  = assetInfo.getAsString("name");
		String tokenId 		  	  = (nonNull(assetInfo.getAsString("token_id"))) ? assetInfo.getAsString("token_id") : null;
		String imageUrl 	  	  = (nonNull(assetInfo.getAsString("image_preview_url"))) ? assetInfo.getAsString("image_preview_url") : null;
		String permalink 	  	  = assetInfo.getAsString("permalink");
		String metadata 	  	  = assetInfo.getAsString("token_metadata");

		JSONObject collectionObj  = (JSONObject) assetInfo.get("collection");
		JSONObject collecObj4Bund = (JSONObject) assetInfo.get("asset_contract");
		JSONObject collection	  = (nonNull(collectionObj)) ? collectionObj : collecObj4Bund;
		String collectionName 	  = collection.getAsString("name");
		String collectionImageUrl = collection.getAsString("image_url");
		String slug			      = collection.getAsString("slug");
		
		// Name formatting for when null
		name = buildNftDisplayName(name, collectionName, tokenId);
		
		// Make calculations about price
		BigDecimal priceInCrypto = null;
		try {
			priceInCrypto = convert.convertToCrypto(priceInWei, decimals);
		} catch (Exception ex) {}
		
		// Rarity lookup
		Rarity r = rUtils.getRarity(c.getContractAddress(), slug, tokenId, metadata, c.getRarityEngine());
		
		// Calculate USD
		BigDecimal priceInUsd = null;
		try {
			BigDecimal ethInUSD = valueLookup.getPriceInUSD(cryptoType);
			priceInUsd = priceInCrypto.multiply(ethInUSD);
		} catch (Exception ex) {
			if(nonNull(paymentToken)) {
				double priceOfOnePayment = Double.parseDouble(usdOfPayment);
				priceInUsd = priceInCrypto.multiply(BigDecimal.valueOf(priceOfOnePayment));
			}
		}
		
		// Process final things to complete the object
		e.setId(id);
		e.setContractAddress(c.getContractAddress());
		e.setName(name);
		e.setCreatedDate(createdDate);
		e.setStartTime(startTime);
		e.setEndTime(endTime);
		e.setTokenId(tokenId);
		e.setCollectionName(collectionName);
		e.setSlug(slug);
		e.setImageUrl(chooseImageUrl(imageUrl, collectionImageUrl));
		e.setCollectionImageUrl(collectionImageUrl);
		e.setLink(permalink);
		e.setQuantity(quantity);
		e.setSellerWalletAddy(sellerWalletAddy);
		e.setSellerName(sellerName);
		e.setSellerUrl(sellerUrl);
		e.setBuyerWalletAddy(buyerWalletAddy);
		e.setBuyerName(buyerName);
		e.setBuyerUrl(buyerUrl);
		e.setRarity(r.getRarity());
		e.setRarityEngine(r.getRarityEngine());
		e.setRarityUrl(r.getRarityUrl());
		e.setPriceInCrypto(priceInCrypto);
		e.setPriceInUsd(priceInUsd);
		e.setCryptoType(cryptoType);
		e.setEventType(eventType);
		e.setMarket(MarketPlace.OPENSEA);
		e.setConsumed(false);
		e.setConfigId(c.getId());
		e.setHash(MarketPlace.OPENSEA, c.getContractAddress(), sellerWalletAddy, String.valueOf(priceInCrypto));
		e.setMetadataUrl(metadata);
		return e;
	}
}
