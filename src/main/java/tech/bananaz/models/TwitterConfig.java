package tech.bananaz.models;

import org.springframework.stereotype.Component;
import lombok.Data;
import tech.bananaz.utils.TwitterUtils;

@Data
@Component
public class TwitterConfig {

	private String address;
	private String apiKey;
	private String apiSecretKey;
	private String accessToken;
	private String accessTokenSecret;
	
	public TwitterUtils configProperties(Config config) throws Exception {
		// Variables of the twitter root node
		this.address  		   = config.getContractAddress();
		this.apiKey 		   = config.getTwitterApiKey();
		this.apiSecretKey 	   = config.getTwitterApiKeySecret();
		this.accessToken 	   = config.getTwitterAccessToken();
		this.accessTokenSecret = config.getTwitterAccessTokenSecret();
		
		return new TwitterUtils(this);
	}

}