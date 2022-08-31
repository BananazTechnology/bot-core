package tech.bananaz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RarityEngine {
	NONE("", "", ""),
	RARITY_TOOLS("raritytools", "https://rarity.tools/%s/view/%s", "rarity.tools"), // NO LOOKUP
    TRAIT_SNIPER("traitsniper", "https://app.traitsniper.com/%s?view=%s", "traitsniper.com"),
    METADATA("metadata", "", "");

	@Getter
    private String engineSlug;
    @Getter
    private String url;
    @Getter
    private String displayName;

    @Override
    public String toString() {
        return this.engineSlug;
    }

    /**
     * Matches from the engineSlug value
     * @param engineSlug
     * @return
     */
    public static RarityEngine fromString(String engineSlug) {
    	for (RarityEngine unit : RarityEngine.values()) {
            if (engineSlug.equalsIgnoreCase(unit.engineSlug)) {
                return unit;
            }
        }
        return RarityEngine.valueOf(engineSlug);
    }
}