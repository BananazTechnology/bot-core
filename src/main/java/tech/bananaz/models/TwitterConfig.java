package tech.bananaz.models;

import static tech.bananaz.encryptors.Security.decrypt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Data;
import tech.bananaz.utils.TwitterUtils;

@Data
@Component
public class TwitterConfig {
	
	@Value("${bot.encryptionKey}")
	private String key;
	private final String NO_KEY_SET = "This application has no internal encryption key set";

	private String address;
	private String apiKey;
	private String apiSecretKey;
	private String accessToken;
	private String accessTokenSecret;
	
	public TwitterUtils configProperties(Config config) throws Exception {
		try {
			// Variables of the twitter root node
			this.address  		   = config.getContractAddress();
			this.apiKey 		   = decrypt(key, config.getTwitterApiKey());
			this.apiSecretKey 	   = decrypt(key, config.getTwitterApiKeySecret());
			this.accessToken 	   = decrypt(key, config.getTwitterAccessToken());
			this.accessTokenSecret = decrypt(key, config.getTwitterAccessTokenSecret());
		} catch (Exception e) {
			throw new Exception(NO_KEY_SET, e.getCause());
		}
		
		return new TwitterUtils(this);
	}

}