package tech.bananaz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Ticker {
    ETH("eth", "Ξ"),
    WETH("weth", "Ξ"),
    BTC("btc", "₿"),
    DAI("dai", "◈"),
    SOL("sol", "◎"),
    USDC("usdc", "$");
	
	@Getter
    private String ticker;
    @Getter
    private String symbol;

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