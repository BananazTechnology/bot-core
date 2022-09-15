package tech.bananaz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
	LIST("list", "listed", "l"),
	SALE("sale", "sold", "s"),
	MINT("mint", "minted", "m"),
	BURN("burn", "burned", "b");
	
	@Getter
    private String event;
    @Getter
    private String verb;
    @Getter
    private String denotion;

    @Override
    public String toString() {
        return this.verb;
    }

    /**
     * unused but would match on event
     * lower-case "sale", "listed", "mint" or "burn" 
     * @param eventType
     * @return
     */
    public static EventType fromString(String eventType) {
    	for (EventType unit : EventType.values()) {
            if (eventType.equalsIgnoreCase(unit.event)) {
                return unit;
            }
        }
        return EventType.valueOf(eventType);
    }
}
