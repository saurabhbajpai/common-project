package com.kaizen.stockwatch;

public class PortfolioStock {
    private String symbol;
    private Integer quantity;
    private Double averageRate;
    private Double overallProfit;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public Double getOverallProfit() {
        return overallProfit;
    }

    public void setOverallProfit(Double overallProfit) {
        this.overallProfit = overallProfit;
    }
}
