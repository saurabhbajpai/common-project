package com.kaizen.stockwatch;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;

import java.io.File;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@Slf4j
public class StockService {

    private static Integer aLittleOverLeastPercentage = 40;
    private static Integer downFromYearHighPercentage = 90; //Current Percentage From Year High
    private static Integer investmentAmount = 10000;
    private static Integer possibleProfitPercentage = 40;

    private static long lowerMarginPercentageT = 60L;
    private static long upperMarginPercentageT = 20L;
    private static long portfolioSellProfitPercentage = 40;
    private static List<Stock> stocks = new ArrayList();
    private static Map<String, PortfolioStock> portfolioStocks = new HashedMap();


    @Scheduled(fixedDelay = 500000000)
    public static void analyse() throws Exception {
        System.out.println("Start : Stock Analysis ########################################################################################################################################################################");

        File[] csvFiles = Utility.csvFinder("C:\\Users\\saura\\Downloads");

        File latestCsvFileWatchlist = null;
        File latestCsvFilePortfolio = null;

        for (File file : csvFiles) {
            if (file.getName().contains("Watchlist") && (latestCsvFileWatchlist == null || file.lastModified() > latestCsvFileWatchlist.lastModified())) {
                latestCsvFileWatchlist = file;
            } else if(file.getName().contains("Portfolio") && (latestCsvFilePortfolio == null || file.lastModified() > latestCsvFilePortfolio.lastModified())) {
                latestCsvFilePortfolio = file;
            }
        }
        System.out.println("Latest Watchlist File Timestamp ----------" + new Date(latestCsvFileWatchlist.lastModified()));
        System.out.println("Latest Portfolio File Timestamp ----------" + new Date(latestCsvFilePortfolio.lastModified()));

        System.out.println("Stock Recommendations --------------------------------------");

        Scanner scannerWatchlist = new Scanner(new File(latestCsvFileWatchlist.getAbsolutePath()));
        Scanner scannerPortfolio = new Scanner(new File(latestCsvFilePortfolio.getAbsolutePath()));
        while (scannerWatchlist.hasNext()) {
            List<String> line = Utility.parseLine(scannerWatchlist.nextLine());

            if (Objects.nonNull(line) && StringUtils.isNotEmpty(line.get(3)) && StringUtils.isNotEmpty(line.get(4))) {
                Stock stock = new Stock();
                stock.setSymbol(line.get(0));
                stock.setExchange(line.get(1));
                stock.setDayHigh(Double.valueOf(line.get(3)));
                stock.setDayLow(Double.valueOf(line.get(4)));
                stock.setYearHigh(Double.valueOf(line.get(7)));
                stock.setYearLow(Double.valueOf(line.get(8)));

                stocks.add(stock);
            }
        }

        while (scannerPortfolio.hasNext()) {
            List<String> line = Utility.parseLine(scannerPortfolio.nextLine());

            if (Objects.nonNull(line) && StringUtils.isNotEmpty(line.get(11)) && Double.valueOf(line.get(11).substring(0,line.get(11).lastIndexOf("%")-1)) >0) {
                PortfolioStock portfolioStock = new PortfolioStock();
                portfolioStock.setSymbol(line.get(0));
                portfolioStock.setQuantity(Integer.valueOf(line.get(1)));
                portfolioStock.setOverallProfit(Double.valueOf(line.get(11).substring(0,line.get(11).lastIndexOf("%")-1)));

                portfolioStocks.put(portfolioStock.getSymbol(), portfolioStock);
            }
        }

        stocks.forEach(
                stock -> {


                    MetaData metaData = new MetaData(stock);

                    formulateConditions(metaData, stock);
                    //printMetaData(stock);
                }
        );
        scannerWatchlist.close();

        System.out.println("Stop  : Stock Analysis ########################################################################################################################################################################");
    }

    private static void formulateConditions(MetaData metaData, Stock stock) {
        PortfolioStock portfolioStock = portfolioStocks.get(metaData.getSymbol());

        BuyOrder buyOrder = new BuyOrder(stock, metaData, investmentAmount);
        SellOrder sellOrder = new SellOrder(stock, metaData);


        if(metaData.getLowerMarginPercentage() < lowerMarginPercentageT) {
           metaData.setAction(Action.BUY);
            System.out.println("Buy  --> " + metaData);
            System.out.println(stock);
            System.out.println(buyOrder);

            System.out.println("-------------------------------------------------------------------------------------------------");

        } else if (metaData.getUpperMarginPercentage() < upperMarginPercentageT) {
            if(portfolioStock != null && portfolioStock.getOverallProfit() > portfolioSellProfitPercentage) {
                metaData.setAction(Action.SELL);
                System.out.println( "Sell --> " + metaData);
                System.out.println(stock);
                System.out.println(sellOrder);

                System.out.println("-------------------------------------------------------------------------------------------------");
            }
        }

    }

//    private static void printMetaData(MetaData metaData) {
//        if(metaData.getAction().equals(Action.BUY)) {
//            double challengePrice = stock.getDayLow() - (swing);
//            double possibleProfit = Math.round(stock.getYearHigh() - challengePrice);
//            double quantityToBuy = Math.round(investmentAmount/challengePrice);
//
//            System.out.println("########## BUY ORDER -------> "
//                    + stock.getSymbol()
//                    + " | Challenge Price :--> " + Math.round(challengePrice)
//                    + " | Quantity :--> " + quantityToBuy
//                    + " | Profit :--> " + quantityToBuy * possibleProfit
//                    + " | Return :--> " + getPercentage(possibleProfit, stock.getYearHigh()) + "%"
//                    + " | Swing-- " + Math.round(swing)
//                    + " | Window-- " + Math.round(window)
//                    + " | LowerMargin-- " + Math.round(lowerMargin)
//                    + " | UpperMargin-- " + Math.round(upperMargin)
//                    + " | LowerMarginP-- " + lowerMarginPercentage + "%"
//                    + " | UpperMarginP-- " + upperMarginPercentage + "%");
//        } else if(metaData.getAction().equals(Action.SELL) & Objects.nonNull(portfolioStock)) {
//            double challengePrice = stock.getDayHigh() + (swing/2);
//
//            System.out.println("########## SELL ORDER -------> "
//                    + portfolioStock.getSymbol()
//                    + " | Order Price :--> " + Math.round(challengePrice)
//                    + " | Standing Profit :--> " + portfolioStock.getOverallProfit() + "%"
//                    + " | Swing-- " + Math.round(swing)
//                    + " | Window-- " + Math.round(window)
//                    + " | LowerMargin-- " + Math.round(lowerMargin)
//                    + " | UpperMargin-- " + Math.round(upperMargin)
//                    + " | LowerMarginP-- " + lowerMarginPercentage + "%"
//                    + " | UpperMarginP-- " + upperMarginPercentage + "%");
//
//        }
//    }

    private static void getRoundOff(Stock stock) {
        Math.round(stock.getDayHigh());
    }

//    private static boolean stockCondition(Stock stock) {
//
//        Double diffYearHighToYearLow = stock.getYearHigh() - stock.getYearLow();
//        Double diffDayLowToYearLow = stock.getDayLow() - stock.getYearLow();
//
//
//        boolean isSuperLeastToday = diffDayLowToYearLow == 0;  //Sure Shot
//
//        long dayLowToYearHigh = getPercentage(stock.getDayLow(), stock.getYearHigh());
//        long marginalUp = getPercentage(diffDayLowToYearLow, diffYearHighToYearLow) ;
//
//
//
//
//        double challengePrice = stock.getDayLow() - (stock.getDayHigh() - stock.getDayLow());
//        double possibleProfit = Math.round(stock.getYearHigh() - challengePrice);
//        double quantityToBuy = Math.round(investmentAmount/challengePrice);
//        double returnPercentage = getPercentage(possibleProfit, stock.getYearHigh());
//
//        boolean isDownFromYearHigh = dayLowToYearHigh < downFromYearHighPercentage;
//        boolean isMarginalUp = marginalUp < aLittleOverLeastPercentage;
//        boolean isGoodPercentageProfit = returnPercentage > possibleProfitPercentage;
//
//
//        if (isSuperLeastToday && isDownFromYearHigh) {
//
//            System.out.println("Sure Shot Requirement for Stock is Meeting $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$: " + stock.getSymbol());
////            System.out.println("Year High to Year Low Difference Amount :----------------- " + Math.round(diffYearHighToYearLow));
////            System.out.println("Today Down from Year High Percentage :-------------------- " + getPercentage(stock.getDayLow(), stock.getYearHigh()) + "%");
//            System.out.println(stock);
//
//            System.out.println("Difference amount from Year High to Year Low:------ " + Math.round(diffYearHighToYearLow));
//            System.out.println("Difference amount from Day Low To Year Low:-------- " + Math.round(diffDayLowToYearLow) );
//
//            System.out.println("Marginal Difference in Percentage:----------------- " + getPercentage(diffDayLowToYearLow, diffYearHighToYearLow) + "%");
//            System.out.println("Down from Year High Percentage:-------------------- " + getPercentage(stock.getDayLow(), stock.getYearHigh()) + "%");
//
//            System.out.println("Challenge Price :--> " + Math.round(challengePrice)
//                    + " | Quantity :--> " + quantityToBuy
//                    + " | Profit :--> " + quantityToBuy * possibleProfit
//                    + " | Return :--> " + getPercentage(possibleProfit, stock.getYearHigh()) + "%");
//
//
//            System.out.println("---------------------------------******************************");
//
//            return true;
//
//        } else if (isMarginalUp && isDownFromYearHigh && isGoodPercentageProfit) {
//
//
//            System.out.println("Marginal Requirement for Stock is Meeting: " + "***** " + stock.getSymbol() + " *****");
//            System.out.println(stock);
//            System.out.println("Difference amount from Year High to Year Low:------ " + Math.round(diffYearHighToYearLow));
//            System.out.println("Difference amount from Day Low To Year Low:-------- " + Math.round(diffDayLowToYearLow) );
//
//            System.out.println("Marginal Difference in Percentage:----------------- " + getPercentage(diffDayLowToYearLow, diffYearHighToYearLow) + "%");
//            System.out.println("Down from Year High Percentage:-------------------- " + getPercentage(stock.getDayLow(), stock.getYearHigh()) + "%");
//
//
//
//            System.out.println("Challenge Price :--> " + Math.round(challengePrice)
//                    + " | Quantity :--> " + quantityToBuy
//                    + " | Profit :--> " + quantityToBuy * possibleProfit
//                    + " | Return :--> " + getPercentage(possibleProfit, stock.getYearHigh()) + "%");
//
//            System.out.println("---------------------------------******************************");
//
//            return true;
//        }
////        else if (possibleProfit > possibleProfitPercentage) {
////            System.out.println("Challenge Price :--> " + Math.round(challengePrice)
////                    + " | Quantity :--> " + Math.round(investmentAmount/challengePrice)
////                    + " | Return :--> " + getPercentage(possibleProfit, stock.getYearHigh()) + "%");
////            return true;
////        }
//        return false;
//
//    }
}
