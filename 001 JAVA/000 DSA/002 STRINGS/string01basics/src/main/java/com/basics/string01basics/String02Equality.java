package com.basics.string01basics;

import java.util.Objects;

public class String02Equality {

    static void basicEquality(){
        String a = "hello";
        String b = "hello";
        String c = new String("hello");
        String d = "HELLO";

        System.out.println(a==b);
        System.out.println(a==c);
        System.out.println(a.equals(b));
        System.out.println(a.equals(c));
        System.out.println(a.equals(d));
        System.out.println(a.equalsIgnoreCase(d));
    }

    static void nullSafePatterns(){
        String status = null;

        System.out.println("OK".equals(status));
        System.out.println(Objects.equals(status,"OK"));
        System.out.println(String.valueOf(status));
    }


    static void main() {
        basicEquality();

    }
}
