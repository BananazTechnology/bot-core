package tech.bananaz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Ticker {
    ETH("eth", "Ξ", null),
    WETH("weth", "Ξ", "eth"),
    BTC("btc", "₿", null),
    DAI("dai", "◈", null),
    SOL("sol", "◎", null),
    USDC("usdc", "$", null),
    MATIC("matic", "Ξ", null);
	
	@Getter
    private String ticker;
    @Getter
    private String symbol;
    @Getter
    private String lookupValue;

    @Override
    public String toString() {
        return this.symbol;
    }

    /**
     * For comparison here we match against the ticker ETH, BTC, SOL
     * @param ticker
     * @return
     */
    public static Ticker fromString(String ticker) {
        if (ticker != null) {
            for (Ticker unit : Ticker.values()) {
                if (ticker.equalsIgnoreCase(unit.ticker)) {
                    return unit;
                }
            }
        }
        return Ticker.valueOf(ticker);
    }
}