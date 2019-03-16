package stocks;

public interface StockService {
    int getPrice(String stockSymbol) throws Exception;
}