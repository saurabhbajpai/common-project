package com.kaizen.stockwatch;

import lombok.Data;

@Data
public class BuyOrder {
    double challengePrice;
    double possibleProfit;
    double quantityToBuy;

    public BuyOrder(Stock stock, MetaData metaData, Integer investmentAmount) {
        this.challengePrice = stock.getDayLow() - (metaData.getSwing()/2);;
        this.possibleProfit = Math.round(stock.getYearHigh() - challengePrice);
        this.quantityToBuy = Math.round(investmentAmount/challengePrice);
    }

    @Override
    public String toString() {
        return "BuyOrder{" +
                "challengePrice=" + challengePrice +
                ", possibleProfit=" + possibleProfit +
                ", quantityToBuy=" + quantityToBuy +
                '}';
    }
}
