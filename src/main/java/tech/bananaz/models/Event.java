package tech.bananaz.models;

import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Event implements Comparable<Event> {
	
	@Id
	private long         id;
	@Column(columnDefinition = "VARCHAR(75)")
	private String       name;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       contractAddress;
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
	@Column(columnDefinition = "VARCHAR(127)")
	private String		 hash;
	// These last few items are for the consumer
	private long         configId;
	@Column(columnDefinition = "VARCHAR(50)")
	private String       consumedBy;
	@Column(nullable = false, columnDefinition="TINYINT(1) UNSIGNED DEFAULT 0")
	private boolean      consumed;
	
	public Event() {}
	
	public void setHash(String contractAddy, String sellerAddy, String priceInCrypto) {
		this.hash = String.format("%s:%s:%s", contractAddy, sellerAddy, priceInCrypto);
	}
	
	@Override
	public int compareTo(Event that) {
		Instant thisCreatedDate = this.createdDate;
		Instant thatCreatedDate = that.getCreatedDate();
		return thatCreatedDate.compareTo(thisCreatedDate); 
	}

}
