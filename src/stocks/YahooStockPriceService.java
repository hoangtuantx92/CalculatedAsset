package stocks;

import java.net.*;
import java.io.*;

public class YahooStockPriceService implements StockService{

    public String getUrlContent(String symbol) throws Exception {
        URL url = new URL("http://download.finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=sl1");

        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line = bufferedReader.readLine();

        bufferedReader.close();
        return line;
    }

    public int getPrice(String symbol) throws Exception{
        try{
            String inputString = getUrlContent(symbol);
            return extractPrice(inputString);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int extractPrice(String inputString){
        try{
            String[] inputArray = inputString.split(",");
            return (int) (Double.parseDouble(inputArray[1]) * 100);
        } catch (Exception e){
            throw new RuntimeException("Invalid stock symbol");
        }
    }
}