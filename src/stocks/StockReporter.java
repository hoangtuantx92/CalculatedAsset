package stocks;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class StockReporter {
    private StockService stockService;

    public int calculateAssetValue(int numberOfShares, int priceInCents){
        return numberOfShares >= 0 && priceInCents >= 0 ? numberOfShares * priceInCents : 0;
    }

    public int calculateNetAssetValue(List<Integer> numbersOfShares, List<Integer> pricesInCents){
        return IntStream.range(0, numbersOfShares.size())
                .map(i -> calculateAssetValue(numbersOfShares.get(i), pricesInCents.get(i)))
                .sum();
    }

    public void setStockService(StockService service){
        stockService = service;
    }

    public List<Stock> getAssetValues(List<Stock> inputStocks){
        return inputStocks.stream()
                .map(this::getAssetValueForAStock)
                .collect(toList());
    }

    public Stock getAssetValueForAStock(Stock inputStock) {
        try {
            return new Stock(inputStock.getSymbol(), inputStock.getNumberOfShares(), stockService.getPrice(inputStock.getSymbol()));
        } catch (Exception ex) {
            return new Stock(inputStock.getSymbol(), inputStock.getNumberOfShares(), ex.getMessage());
        }
    }
}