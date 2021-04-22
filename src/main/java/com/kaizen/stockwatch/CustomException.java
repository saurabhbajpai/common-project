package com.kaizen.stockwatch;

public class CustomException extends Exception{
    public  String error(int code) throws Exception {
        throw new Exception("Program Error");
    }
}
