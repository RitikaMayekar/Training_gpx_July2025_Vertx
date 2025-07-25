package com.globalpayex.utils;

public class Series {
    public static String fiboGenerator(int num){

        if(num<2){
            throw new IllegalArgumentException("num cannot be less than 2");
        }

        int n1 = 0, n2=1;
        String fib ="";
        fib += n1+","+n2;

        for(int i=3; i<=num;i++){
            int n3 = n1 + n2;
            fib += ","+n3;
            n1 = n2;
            n2 = n3;
        }
        return fib;
    }

}
