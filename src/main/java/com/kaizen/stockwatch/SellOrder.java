package com.kaizen.stockwatch;

import lombok.Data;

@Data
public class SellOrder {
    double challengePrice;
    double possibleProfit;


    public SellOrder(Stock stock, MetaData metaData) {
        this.challengePrice = stock.getDayHigh() + (metaData.getSwing()/2);;
        this.possibleProfit = Math.round(stock.getYearHigh() - challengePrice);
    }

    @Override
    public String toString() {
        return "Order{" +
                "challengePrice=" + challengePrice +
                ", possibleProfit=" + possibleProfit +
                '}';
    }
}
