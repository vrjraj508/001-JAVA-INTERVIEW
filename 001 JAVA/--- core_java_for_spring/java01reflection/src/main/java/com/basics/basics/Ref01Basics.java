package com.basics.basics;


abstract class Animal{
    public String name;
}

interface Flyable{
    void fly();
}

interface Swimmable{
    void swim();
}

 class Duck extends Animal implements Flyable,Swimmable{

    @Override
    public void fly() {
        System.out.println("Duck is flying");
    }

    @Override
    public void swim() {
        System.out.println("Duck is swimming");
    }
}

public class Ref01Basics {

    public static void main(String[] args) {
        Class<?> clazz = Duck.class;


        System.out.println(clazz.getName());
        System.out.println(clazz.getSimpleName());
        System.out.println(clazz.getCanonicalName());
        System.out.println(clazz.getPackageName());
    }
}
