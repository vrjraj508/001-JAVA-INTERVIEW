package com.reflection.java01basics;

public class Reflection01Basics {

    public static void main(String[] args) {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;

        System.out.println(stringClass.getName());
        System.out.println(stringClass.getSimpleName());

        Object obj = "Hello World";

        Class<?> runtimeClass  = obj.getClass();

        System.out.println(runtimeClass.getName());
    }

}
