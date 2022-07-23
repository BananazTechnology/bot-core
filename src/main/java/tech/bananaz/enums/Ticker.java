package tech.bananaz.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Ticker {
    ETH("eth", "Ξ"),
    WETH("weth", "Ξ"),
    BTC("btc", "₿"),
    DAI("dai", "◈"),
    SOL("sol", "◎");

    private String ticker;
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return ticker;
    }

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