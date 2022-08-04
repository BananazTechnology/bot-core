package tech.bananaz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.bananaz.enums.RarityEngine;

@AllArgsConstructor
@Data
public class Rarity {
	
	String rarity;
	String rarityUrl;
	RarityEngine rarityEngine;
	
	public Rarity() {}

}
