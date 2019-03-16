package stocks;

public class Stock {
    private final String symbol;
    private final int numberOfShares;
    private final int assetValue;
    private final String error;

    public Stock(String stockSymbol, int shareCount){
        symbol = stockSymbol;
        numberOfShares = shareCount;
        assetValue = 0;
        error = "";
    }

    public Stock(String stockSymbol, int shareCount, int value){
        symbol = stockSymbol;
        numberOfShares = shareCount;
        assetValue = value;
        error = "";
    }

    public Stock(String stockSymbol, int shareCount, String errorMessage){
        symbol = stockSymbol;
        numberOfShares = shareCount;
        assetValue = 0;
        error = errorMessage;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getNumberOfShares(){
        return numberOfShares;
    }

    public int getAssetValue() {
        return assetValue;
    }

    public String getError() {
        return error;
    }
}
