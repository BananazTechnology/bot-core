package tech.bananaz.utils;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.*;
import org.javacord.api.entity.message.*;
import org.javacord.api.entity.message.embed.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bananaz.models.Config;
import tech.bananaz.models.DiscordConfig;
import tech.bananaz.models.Event;
import static tech.bananaz.utils.StringUtils.capitalizeFirstLetter;
import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

public class DiscordUtils {
	
	/** Required */
	private DiscordApi bot = null;
	private ServerTextChannel channel;
	private Color color;
	
	/** Custom */
	private final static String NEWLINE 	   = "\n";
	private static final String CREATORNAME    = "@BananazTech";
	private static final String FOOTERIMAGEICO = "https://raw.githubusercontent.com/BananazTechnology/bananaz-assets/main/assets/navLogo.png";
	private static final Logger LOGGER  	   = LoggerFactory.getLogger(DiscordUtils.class);
	private StringUtils sUtils 				   = new StringUtils();
	
	public DiscordUtils(DiscordConfig config) {
		if(nonNull(config.getDiscordToken()) && nonNull(config.getChannelId())) {
			// Color - unlikely to fail
			try {
				this.color = config.getMessageColor();
			} catch (Exception e) {}
			
			// Bot builder
			try {
		        this.bot = new DiscordApiBuilder().setToken(config.getDiscordToken()).login().join();
			} catch (Exception e) {
				LOGGER.error("Failed starting bot! Exception: " + e.getMessage());
	        	throw new RuntimeException(e.getMessage());
			}
			
			// Channel grabber
			try {
		        this.channel = this.bot.getChannelById(config.getChannelId()).get().asServerTextChannel().get();
			} catch (Exception e) {
				Collection<ServerTextChannel> availChannels = this.bot.getServerTextChannels();
				
				LOGGER.error("Failed getting channel with id {}! Available channel id's are {}", config.getChannelId(), Arrays.toString(availChannels.toArray()));
	        	throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	public void sendEvent(Event event, Config c) {
		logSend();
		if(nonNull(this.bot)) {
			// Build rarity value
			String finalRarity = 
				(nonNull(event.getRarity())) ? 
					String.format("**Rank** [%s](%s) on %s \n", event.getRarity(), event.getRarityUrl(), event.getRarityEngine().getDisplayName()) : 
					"";
			// Build title
			String title = String.format("%s %s! (%s)", 
											event.getName(), 
											capitalizeFirstLetter(event.getEventType().toString()), 
											event.getMarket().getSlug());
			// Set buyer field for a sale or blank
			String buyerText = (nonNull(event.getBuyerWalletAddy())) ?  "**Buyer**  " + String.format("[`%s`](%s) \n", event.getBuyerName(), event.getBuyerUrl()) : "";
			// Build embed
			EmbedBuilder newMsg = new EmbedBuilder()
				.setColor(this.color)
				.setAuthor(title)
				.setThumbnail(event.getImageUrl())
				.setDescription(
						finalRarity + 
					   ((event.getQuantity() > 1) ? "**Bundle** " + event.getQuantity() + "x \n" : "") +
					   "**Amount** " + sUtils.pricelineFormat(event.getPriceInCrypto(), event.getCryptoType(), event.getPriceInUsd()) + NEWLINE +
					   buyerText +
					   "**Seller** " + String.format("[`%s`](%s) \n", event.getSellerName(), event.getSellerUrl()) +
					   "**Link**   " + String.format("[Click Here](%s)", event.getLink())
				   )
				.setTimestamp(event.getCreatedDate())
				.setFooter(CREATORNAME, FOOTERIMAGEICO);
			
			new MessageBuilder().setEmbed(newMsg).send(this.channel);
		}
	}
	
	public void logSend() {
		LOGGER.debug("messaged triggered, connection={}", this.bot);
	}
	
	public boolean isTokenEqual(String thatToken) {
		if(nonNull(this.bot) && nonNull(thatToken)) return this.bot.getToken().equals(thatToken);
		return false;
	}

	public boolean isChannelIdEqual(String thatId) {
		if(isNull(this.bot)) return true;
		if(nonNull(this.bot) && nonNull(thatId)) return this.channel.getIdAsString().equals(thatId);
		return false;
	}

	public void setServerTextChannel(String id) {
		if(nonNull(this.bot)) this.channel = this.bot.getChannelById(id).get().asServerTextChannel().get();
	}

	public boolean isColorRgbEqual(Color thatRGB) {
		if(nonNull(this.color) && nonNull(thatRGB)) return this.color.equals(thatRGB);
		return false;
	}

	public void setColor(Color colorRGB) {
		if(nonNull(colorRGB)) this.color = colorRGB;
	}
	
	public DiscordApi getBot() {
		return this.bot;
	}
}