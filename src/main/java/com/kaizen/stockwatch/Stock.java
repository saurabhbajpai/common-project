package com.kaizen.stockwatch;

import lombok.*;


@Data
@ToString
public class Stock {
    private String symbol;
    private String exchange;
    private Double dayHigh;
    private Double dayLow;
    private Double yearHigh;
    private Double yearLow;
}
