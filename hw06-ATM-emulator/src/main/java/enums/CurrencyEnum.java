package enums;

public enum CurrencyEnum {

    RUB("Российский рубль", "RUB", "\u0584", new int[]{10, 50, 100, 200, 500, 1000, 2000, 5000}),
    USD("Американский доллар", "USD", "$", new int[]{1,100}),
    EURO("Единая Европейская валюта", "EUR", "\u20ac", new int[]{1,10});

    public int[] getNominals() {
        return nominals;
    }

    private final String fullName;
    private final String shortName;
    private final String symbol;
    private final int[] nominals;


    CurrencyEnum(String fullName, String shortName, String symbol, int[] nominals) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.symbol = symbol;
        this.nominals = nominals;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getSymbol() {
        return symbol;
    }
}
