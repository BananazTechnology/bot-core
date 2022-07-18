package tech.bananaz.models;

import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.ToString;
import tech.bananaz.enums.EventType;
import tech.bananaz.enums.MarketPlace;
import tech.bananaz.enums.RarityEngine;
import tech.bananaz.enums.Ticker;

@ToString(includeFieldNames=true)
@Data
@Entity
@Table(name = "event")
public class Event {
	
	@Id
	private long         id;
	@Column(columnDefinition = "VARCHAR(75)")
	private String       name;
	private Instant      createdDate;
	private Instant      startTime;
	private Instant      endTime;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       tokenId;
	@Column(columnDefinition = "VARCHAR(75)")
	private String       collectionName;
	private String       collectionImageUrl;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       slug;
	private String       imageUrl;
	@Column(columnDefinition = "VARCHAR(127)")
	private String       permalink;
	private int	         quantity;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       sellerWalletAddy;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       sellerName;
	@Column(columnDefinition = "VARCHAR(127)")
	private String       sellerUrl;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       buyerWalletAddy;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       buyerName;
	@Column(columnDefinition = "VARCHAR(127)")
	private String       buyerUrl;
	@Column(columnDefinition = "VARCHAR(6)")
	private String       rarity;
	@Enumerated( EnumType.STRING )
	@Column(columnDefinition = "VARCHAR(50)")
	private RarityEngine rarityEngine;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       rarityUrl;
	@Column(columnDefinition = "VARCHAR(25)")
	private BigDecimal   priceInCrypto;
	@Column(columnDefinition = "VARCHAR(25)")
	private BigDecimal   priceInUsd;
	@Enumerated( EnumType.STRING )
	@Column(columnDefinition = "VARCHAR(6)")
	private Ticker       cryptoType;
	@Enumerated( EnumType.STRING )
	@Column(columnDefinition = "VARCHAR(7)")
	private EventType    eventType;
	@Enumerated( EnumType.STRING )
	@Column(columnDefinition = "VARCHAR(50)")
	private MarketPlace  market;
	// These last few items are for the consumer
	private long         configId;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       consumedBy;
	@Column(nullable = false, columnDefinition="TINYINT(1) UNSIGNED DEFAULT 0")
	private boolean      consumed;

}
