package tech.bananaz.models;

import java.awt.Color;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Data;
import static tech.bananaz.encryptors.Security.decrypt;
import tech.bananaz.utils.DiscordUtils;
import static java.util.Objects.nonNull;

@Data
@Component
public class DiscordConfig {
	
	@Value("${bot.encryptionKey}")
	private String key;
	private final String NO_KEY_SET = "This application has no internal encryption key set";
	
	private String discordToken;
	private Color messageColor;
	private String serverId;
	private String channelId;
	private String address;

	public DiscordUtils configProperties(Config config) throws Exception {
		try {
			// Variables of the discord root node
			this.address    	= config.getContractAddress();
			this.discordToken   = decrypt(key, config.getDiscordToken());
			this.serverId       = config.getDiscordServerId();
			this.channelId      = config.getDiscordChannelId();
			this.messageColor   = (nonNull(config.getDiscordMessageColor())) ? 
									new Color(config.getDiscordMessageColor()) :
										Color.ORANGE;
		} catch (Exception e) {
			throw new Exception(NO_KEY_SET, e.getCause());
		}
		
		return new DiscordUtils(this);
	}

}
