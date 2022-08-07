package tech.bananaz.utils;

import tech.bananaz.models.Listing;
import tech.bananaz.models.Sale;
import static java.util.Objects.nonNull;
import static tech.bananaz.encryptors.Security.encrypt;
import static tech.bananaz.encryptors.Security.decrypt;

public class EncryptionUtils {
	
	public static Sale encryptSale(String key, Sale sale) throws Exception {
		if(nonNull(sale.getDiscordToken())) sale.setDiscordToken(encrypt(key, sale.getDiscordToken()));
		if(nonNull(sale.getTwitterApiKey())) sale.setTwitterApiKey(encrypt(key, sale.getTwitterApiKey()));
		if(nonNull(sale.getTwitterApiKeySecret())) sale.setTwitterApiKeySecret(encrypt(key, sale.getTwitterApiKeySecret()));
		if(nonNull(sale.getTwitterAccessToken())) sale.setTwitterAccessToken(encrypt(key, sale.getTwitterAccessToken()));
		if(nonNull(sale.getTwitterAccessTokenSecret())) sale.setTwitterAccessTokenSecret(encrypt(key, sale.getTwitterAccessTokenSecret()));
		
		return sale;
	}
	
	public static Listing encryptListing(String key, Listing listing) throws Exception {
		if(nonNull(listing.getDiscordToken())) listing.setDiscordToken(encrypt(key, listing.getDiscordToken()));
		if(nonNull(listing.getTwitterApiKey())) listing.setTwitterApiKey(encrypt(key, listing.getTwitterApiKey()));
		if(nonNull(listing.getTwitterApiKeySecret())) listing.setTwitterApiKeySecret(encrypt(key, listing.getTwitterApiKeySecret()));
		if(nonNull(listing.getTwitterAccessToken())) listing.setTwitterAccessToken(encrypt(key, listing.getTwitterAccessToken()));
		if(nonNull(listing.getTwitterAccessTokenSecret())) listing.setTwitterAccessTokenSecret(encrypt(key, listing.getTwitterAccessTokenSecret()));
		
		return listing;
	}
	
	public static Sale decryptSale(String key, Sale sale) throws Exception {
		if(nonNull(sale.getDiscordToken())) sale.setDiscordToken(decrypt(key, sale.getDiscordToken()));
		if(nonNull(sale.getTwitterApiKey())) sale.setTwitterApiKey(decrypt(key, sale.getTwitterApiKey()));
		if(nonNull(sale.getTwitterApiKeySecret())) sale.setTwitterApiKeySecret(decrypt(key, sale.getTwitterApiKeySecret()));
		if(nonNull(sale.getTwitterAccessToken())) sale.setTwitterAccessToken(decrypt(key, sale.getTwitterAccessToken()));
		if(nonNull(sale.getTwitterAccessTokenSecret())) sale.setTwitterAccessTokenSecret(decrypt(key, sale.getTwitterAccessTokenSecret()));
		
		return sale;
	}
	
	public static Listing decryptListing(String key, Listing listing) throws Exception {
		if(nonNull(listing.getDiscordToken())) listing.setDiscordToken(decrypt(key, listing.getDiscordToken()));
		if(nonNull(listing.getTwitterApiKey())) listing.setTwitterApiKey(decrypt(key, listing.getTwitterApiKey()));
		if(nonNull(listing.getTwitterApiKeySecret())) listing.setTwitterApiKeySecret(decrypt(key, listing.getTwitterApiKeySecret()));
		if(nonNull(listing.getTwitterAccessToken())) listing.setTwitterAccessToken(decrypt(key, listing.getTwitterAccessToken()));
		if(nonNull(listing.getTwitterAccessTokenSecret())) listing.setTwitterAccessTokenSecret(decrypt(key, listing.getTwitterAccessTokenSecret()));
		
		return listing;
	}

}
