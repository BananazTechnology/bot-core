package tech.bananaz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
	LIST("list", "listed"),
	SALE("sale", "sold"),
	MINT("mint", "minted"),
	BURN("burn", "burned");
	
	@Getter
    private String event;
    @Getter
    private String verb;

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
