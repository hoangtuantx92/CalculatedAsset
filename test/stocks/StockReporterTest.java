package stocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class StockReporterTest {
    StockReporter stockReporter;
    StockService stockService;

    @BeforeEach
    void init(){
        stockReporter = new StockReporter();
        stockService = mockGetPriceDependency();
    }

    @Test
    void canaryTest(){
        assertTrue(true);
    }

    @Test
    void calculateAssetsWithOneTicker(){
        assertEquals(2000, stockReporter.calculateAssetValue(1000, 2));
    }

    @Test
    void calculateAssetsWithNegativePrice(){
        assertEquals(0, stockReporter.calculateAssetValue(1000, -1));
    }

    @Test
    void calculateAssetsWithZeroPrice(){
        assertEquals(0, stockReporter.calculateAssetValue(1000, 0));
    }

    @Test
    void calculateAssetsWithZeroShares(){
        assertEquals(0, stockReporter.calculateAssetValue(0, 1000));
    }

    @Test
    void calculateAssetsWithNegativeShares(){
        assertEquals(0, stockReporter.calculateAssetValue(-5, 20000));
    }

    @Test
    void computeNetAssetValue(){
        List<Integer> numberOfSharesList = Arrays.asList(500, 100, 400, 300);
        List<Integer> priceInCentsList = Arrays.asList(4000, 3000, 20000, 6000);

        assertEquals(12100000, stockReporter.calculateNetAssetValue(numberOfSharesList, priceInCentsList));
    }

    @Test
    void computeNetAssetValueWithNegativePrice(){
        List<Integer> numberOfSharesList = Arrays.asList(500, 100, 400, 300);
        List<Integer> priceInCentsList = Arrays.asList(4000, 3000, 20000, -6000);

        assertEquals(10300000, stockReporter.calculateNetAssetValue(numberOfSharesList, priceInCentsList));
    }

    @Test
    void computeNetAssetWithOneNegativeShare(){
        List<Integer> numberOfSharesList = Arrays.asList(-2);
        List<Integer> priceInCentsList = Arrays.asList(1000);

        assertEquals(0, stockReporter.calculateNetAssetValue(numberOfSharesList, priceInCentsList));
    }

    private StockService mockGetPriceDependency(){
        StockService stockService = mock(StockService.class);
        try {
            when(stockService.getPrice("XYZ1")).thenReturn(9000);
            when(stockService.getPrice("XYZ2")).thenReturn(10000);
            when(stockService.getPrice("XYZ3")).thenThrow(new RuntimeException("Invalid stock symbol"));
            when(stockService.getPrice("XYZJ")).thenThrow(new RuntimeException("Error reaching the network"));
        } catch (Exception e){

        }
        return stockService;
    }

    @Test
    void getPriceOfOneShare(){
        List<Stock> inputList = new ArrayList<>();
        inputList.add(new Stock("XYZ1", 4));

        stockReporter.setStockService(stockService);
        List <Stock> stockList = stockReporter.getAssetValues(inputList);

        assertEquals(9000, stockList.get(0).getAssetValue());
    }

    @Test
    void getPriceOfTwoShares(){
        List<Stock> inputList = new ArrayList<>();
        inputList.add(new Stock("XYZ1", 5));
        inputList.add(new Stock("XYZ2", 1));

        stockReporter.setStockService(stockService);
        List <Stock> stockList = stockReporter.getAssetValues(inputList);

        assertEquals(9000, stockList.get(0).getAssetValue());
        assertEquals(10000, stockList.get(1).getAssetValue());
    }

    @Test
    void getPriceWithInvalidStockError(){
        List<Stock> inputList = new ArrayList<>();
        inputList.add(new Stock("XYZ3", 10));

        stockReporter.setStockService(stockService);
        List <Stock> stockList = stockReporter.getAssetValues(inputList);

        assertEquals("Invalid stock symbol", stockList.get(0).getError());
    }

    @Test
    void getPriceOfThreeSharesWithNetworkError(){
        List<Stock> inputList = new ArrayList<>();
        inputList.add(new Stock("XYZ1", 10));
        inputList.add(new Stock("XYZ2", 3));
        inputList.add(new Stock("XYZJ", 2));

        stockReporter.setStockService(stockService);
        List <Stock> stockList = stockReporter.getAssetValues(inputList);

        assertEquals(9000, stockList.get(0).getAssetValue());
        assertEquals(10000, stockList.get(1).getAssetValue());
        assertEquals("Error reaching the network", stockList.get(2).getError());
    }
}