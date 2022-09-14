package tech.bananaz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.MediaCategory;
import com.github.redouane59.twitter.dto.tweet.UploadMediaResponse;
import com.github.redouane59.twitter.signature.*;
import static tech.bananaz.utils.StringUtils.capitalizeFirstLetter;
import static tech.bananaz.utils.StringUtils.determineFileType;
import static tech.bananaz.utils.StringUtils.stringTemplateFormatEvent;
import static tech.bananaz.utils.UrlUtils.imageUrlToBytes;
import tech.bananaz.models.Config;
import tech.bananaz.models.Event;
import tech.bananaz.models.TwitterConfig;
import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

public class TwitterUtils {
	
	private String apiKey;
	private String apiSecret;
	private TwitterClient bot          = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(TwitterUtils.class);

	public TwitterUtils(TwitterConfig config) {
		if(nonNull(config.getApiKey()) && nonNull(config.getApiSecretKey()) && nonNull(config.getAccessToken())) {
			try {
				this.apiKey    = config.getApiKey();
				this.apiSecret = config.getApiSecretKey();
				this.bot = new TwitterClient(TwitterCredentials.builder()
	                .accessToken(config.getAccessToken())
	                .accessTokenSecret(config.getAccessTokenSecret())
	                .apiKey(config.getApiKey())
	                .apiSecretKey(config.getApiSecretKey())
	                .build());
			} catch (Exception e) {
				LOGGER.error("Failed starting bot! Exception: " + e.getMessage());
	        	throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	public void sendEvent(Event event, Config c) {
		logSend();
		if(this.bot != null) { 
			// Since our template formatting fork we need defaults
			final String defaultTemplate = ":name:%s\n%s for :priceInCrypto: :priceInUsd:\n:link:";
			// Enables features for Auto Rarity and custom rarity clients
			final String defaultRarity = (nonNull(event.getRarity()) && nonNull(event.getRarityEngine())) ? "\nRank: " + event.getRarity() : "";
			String template = String.format(defaultTemplate, defaultRarity, capitalizeFirstLetter(event.getEventType().getVerb()));
			String imageAttachmentId = null;
			
			// If our Twitter Template is not null, not empty and not blank then grab it
			if(nonNull(c.getTwitterMessageTemplate()))
				if(!c.getTwitterMessageTemplate().isBlank() && !c.getTwitterMessageTemplate().isEmpty())
					template = c.getTwitterMessageTemplate();
			
			// This function does all core formatting on the template string
			String renderedBody = stringTemplateFormatEvent(template, event);
			
			// For Twitter template only
			// This section below supports :image: function in template
			/// It will upload the resource at the imageUrl to Twitter as media
			/// It is much easier to search strings when in all lower-case
			final String renderedBodyLC = renderedBody.toLowerCase();
			/// Our special function to search for
			final String CUSTOM_TWITTER_IMAGE_ATTACHMNT = ":image:";
			// If our lower-case template has out lower-case key
			if(renderedBodyLC.contains(CUSTOM_TWITTER_IMAGE_ATTACHMNT)) {
				// Calculate the location of our key in the template
				int keyStart = renderedBodyLC.indexOf(CUSTOM_TWITTER_IMAGE_ATTACHMNT);
				int keyEnd = keyStart+CUSTOM_TWITTER_IMAGE_ATTACHMNT.length();
				// Select a case-specific version of how the key was typed
				String keyInTemplate = renderedBody.substring(keyStart, keyEnd);
				// Remove the key
				renderedBody = renderedBody.replace(keyInTemplate, "");
				// Upload the imageUrl and save the ID of the media post
				imageAttachmentId = sendAsset(event.getName(), event.getImageUrl());
			}
			
			// Post the tweet any any media attachments
			bot.postTweet(renderedBody, null, imageAttachmentId);
		}
	}
	
	private String sendAsset(String name, String url) {
		MediaCategory fileType = determineFileType(url);
		// Upload asset
		UploadMediaResponse asset = this.bot.uploadMedia(name, imageUrlToBytes(url), fileType);
		// Return asset id for attachments
		return asset.getMediaId();
	}
	
	public void logSend() {
		LOGGER.debug("message triggered, botConnection={}", this.bot);
	}

	public boolean apiKeyEquals(String thatAPIKey) {
		if(isNull(this.apiKey) && isNull(thatAPIKey)) return true;
		if(nonNull(this.apiKey)) return this.apiKey.equals(thatAPIKey);
		return false;
		
	}

	public boolean apiKeySecretEquals(String thatAPIKeySecret) {
		if(isNull(this.apiSecret) && isNull(thatAPIKeySecret)) return true;
		if(nonNull(this.apiSecret)) return this.apiSecret.equals(thatAPIKeySecret);
		return false;
	}
	
	public TwitterClient getBot() {
		return this.bot;
	}

}