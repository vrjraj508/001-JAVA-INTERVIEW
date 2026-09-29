package com.basics.basics.dsa01strings;

public class String01Basics {

    public static void stringOp001(String s){
        for(int i=0;i<s.length();i++){
            System.out.print(s.charAt(i)+" ");
        }
    }

    public static void main(String[] args) {

        stringOp001("Viraj Kumar is so good");

    }
}
