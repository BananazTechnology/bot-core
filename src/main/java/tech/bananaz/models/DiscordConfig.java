package tech.bananaz.models;

import java.awt.Color;
import org.springframework.stereotype.Component;
import lombok.Data;
import tech.bananaz.utils.DiscordUtils;
import static java.util.Objects.nonNull;

@Data
@Component
public class DiscordConfig {
	
	private String discordToken;
	private Color messageColor;
	private String serverId;
	private String channelId;
	private String address;

	public DiscordUtils configProperties(Config config) throws Exception {
		// Variables of the discord root node
		this.address    	= config.getContractAddress();
		this.discordToken   = config.getDiscordToken();
		this.serverId       = config.getDiscordServerId();
		this.channelId      = config.getDiscordChannelId();
		this.messageColor   = (nonNull(config.getDiscordMessageColor())) ? 
								new Color(config.getDiscordMessageColor()) :
									Color.ORANGE;
		
		return new DiscordUtils(this);
	}

}
