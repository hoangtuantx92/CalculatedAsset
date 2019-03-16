package stocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class YahooStockPriceServiceTest {
    YahooStockPriceService priceService;

    @BeforeEach
    void init(){
        priceService= new YahooStockPriceService();
    }

    @Test
    void getContentFromURl() throws Exception {
        String dataURL = priceService.getUrlContent("AAPL");
        String[] dataArray = dataURL.split(",");
        assertEquals("\"AAPL\"", dataArray[0]);
    }

    @Test
    void getStockPriceOnlineWithOneSymbol() throws Exception {
        assertTrue(priceService.getPrice("AAPL") > 0);
    }

    @Test
    void throwsInvalidStockSymbol() throws Exception{
        Exception message = (assertThrows(RuntimeException.class, () -> priceService.getPrice("INVALID")));

        assertEquals("Invalid stock symbol",message.getMessage());
    }

    @Test
    void throwsFailNetworkConnection() throws Exception{
        YahooStockPriceService mockNetworkDependency = spy(new YahooStockPriceService());
        doThrow(new RuntimeException("Error reaching the network")).when(mockNetworkDependency).getUrlContent("AAPL");

        Exception message = assertThrows(RuntimeException.class, () -> mockNetworkDependency.getPrice("AAPL"));
        assertEquals("Error reaching the network", message.getMessage());
    }

    @Test
    void callsGetPriceToGetActualValue() throws Exception {
        String urlContent = priceService.getUrlContent("AAPL");
        String[] contentArray = urlContent.split(",");
        assertEquals(String.format("%.2f",Double.parseDouble(contentArray[1])), String.format("%.2f",(double) (priceService.extractPrice(urlContent))/100));
    }
}