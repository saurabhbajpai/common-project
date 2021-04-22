package com.kaizen.stockwatch;

import java.util.ArrayList;
import java.util.List;

public class Average {
    int sum = 0;

    public static void main(String args[]) {
        List myList = new ArrayList<Integer>();

        myList.add(10);
        myList.add(20);
        myList.add(30);

        System.out.println(myList.stream().mapToInt(value -> (int) value).average().getAsDouble());


    }

}
