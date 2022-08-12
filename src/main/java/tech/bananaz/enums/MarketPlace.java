package tech.bananaz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MarketPlace {
    OPENSEA("opensea", "OS"),
    LOOKSRARE("looksrare", "LR");
	
    @Getter
    private String displayName;
    @Getter
    private String slug;

    @Override
    public String toString() {
        return this.slug;
    }

    /**
     * unused but would match on displayName 
     * @param displayName
     * @return
     */
    public static MarketPlace fromString(String displayName) {
    	for (MarketPlace unit : MarketPlace.values()) {
            if (displayName.equalsIgnoreCase(unit.displayName)) {
                return unit;
            }
        }
        return MarketPlace.valueOf(displayName);
    }
}