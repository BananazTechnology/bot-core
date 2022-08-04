package tech.bananaz.models;

import lombok.Data;
import tech.bananaz.utils.TwitterUtils;

@Data
public class TwitterConfig {

	private String address;
	private String apiKey;
	private String apiSecretKey;
	private String accessToken;
	private String accessTokenSecret;
	
	public TwitterUtils configProperties(Config config) {
		// Variables of the discord root node
		this.address  		   = config.getContractAddress();
		this.apiKey 		   = config.getTwitterApiKey();
		this.apiSecretKey 	   = config.getTwitterApiKeySecret();
		this.accessToken 	   = config.getTwitterAccessToken();
		this.accessTokenSecret = config.getTwitterAccessTokenSecret();
		
		return new TwitterUtils(this);
	}

}