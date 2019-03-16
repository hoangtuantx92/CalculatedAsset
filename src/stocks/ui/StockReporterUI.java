package stocks.ui;

import stocks.Stock;
import stocks.StockReporter;
import stocks.YahooStockPriceService;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StockReporterUI {
    StockReporter stockReporter = new StockReporter();
    YahooStockPriceService yahooService = new YahooStockPriceService();

    public static void main(String[] args) throws IOException{
        StockReporterUI reporterUI = new StockReporterUI();

        List<Integer> priceList = new ArrayList<>();
        List<Integer> shareList = new ArrayList<>();

        try{
            Scanner scanStockFile = new Scanner(new FileReader((args[0])));
            List<Stock> inputStocks = reporterUI.populateListFromInput(scanStockFile);

            reporterUI.stockReporter.setStockService(reporterUI.yahooService);
            List<Stock> completedList = reporterUI.stockReporter.getAssetValues(inputStocks);

            System.out.println("Symbol  Shares  Net Asset Value\n-------------------------------");
            reporterUI.printListWithValue(completedList);
            reporterUI.createListsForTotal(priceList, shareList, completedList);
            System.out.println("Total   $" + String.format("%.2f", (double) reporterUI.stockReporter.calculateNetAssetValue(shareList, priceList)/100));

            System.out.println("\nErrors:" );
            reporterUI.printErrorStocks(completedList);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void printListWithValue(List<Stock> completedList){
        for(Stock stock : completedList){
            if (stock.getAssetValue() > 0) {
                System.out.println(stock.getSymbol() + "     " + stock.getNumberOfShares() + "     $" + String.format("%.2f", (double) stockReporter.calculateAssetValue(stock.getNumberOfShares(), stock.getAssetValue())/100));
            }
        }
    }

    private void printErrorStocks(List<Stock> completedList){
        for (Stock stock : completedList){
            if(stock.getAssetValue() == 0){
                System.out.println(stock.getSymbol() + "  " + stock.getError());
            }
        }
    }

    private List<Stock> populateListFromInput(Scanner fileScanner){
        List<Stock> inputList = new ArrayList<>();

        while(fileScanner.hasNext()){
            inputList.add(new Stock(fileScanner.next(), Integer.parseInt(fileScanner.next())));
        }
        return inputList;
    }

    private void createListsForTotal(List<Integer> priceList, List<Integer> shareList, List<Stock> completedList){
        for(Stock stock : completedList){
            priceList.add(stock.getAssetValue());
            shareList.add(stock.getNumberOfShares());
        }
    }
}