package com.kaizen.stockwatch;

import lombok.Data;

import static com.kaizen.stockwatch.Utility.getPercentage;

@Data
public class MetaData {


    public MetaData(Stock stock) {
        this.symbol = stock.getSymbol();
        this.swing = (int) Math.round(stock.getDayHigh() - stock.getDayLow());
        this.window = (int) Math.round(stock.getYearHigh() - stock.getYearLow());
        this.lowerMargin = (int) Math.round(stock.getDayLow() - stock.getYearLow());
        this.upperMargin = (int) Math.round(stock.getYearHigh() - stock.getDayHigh());
        this.lowerMarginPercentage = getPercentage(stock.getDayLow() - stock.getYearLow(), stock.getYearHigh() - stock.getYearLow());
        this.upperMarginPercentage = getPercentage(stock.getYearHigh() - stock.getDayHigh(), stock.getYearHigh() - stock.getYearLow());
    }

    private String symbol;
    private Action action;
    private int swing;
    private int window;
    private int lowerMargin;
    private int upperMargin;
    private long lowerMarginPercentage;
    private long upperMarginPercentage;


    @Override
    public String toString() {
        return "MetaData{" +
                "symbol='" + symbol + '\'' +
                ", action=" + action +
                ", swing=" + swing +
                ", window=" + window +
                ", lowerMargin=" + lowerMargin +
                ", upperMargin=" + upperMargin +
                ", lowerMarginPercentage=" + lowerMarginPercentage +
                ", upperMarginPercentage=" + upperMarginPercentage +
                '}';
    }
}
