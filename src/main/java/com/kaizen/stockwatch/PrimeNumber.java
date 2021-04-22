package com.kaizen.stockwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PrimeNumber {

    public static void main(String args[]) {
        int[] intArray = {1,2,3,4,5,6,7,8,9};

        List eventList = new ArrayList();
        List listArrayAll = Arrays.asList(intArray.clone());

        listArrayAll.stream().filter(new Predicate() {
           @Override
           public boolean test(Object o) {
               // Divisible by 2
               return true;
           }
       }).collect(Collectors.toList());

    }
}
