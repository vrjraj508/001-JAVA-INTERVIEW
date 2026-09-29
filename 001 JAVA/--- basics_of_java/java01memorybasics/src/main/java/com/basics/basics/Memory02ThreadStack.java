package com.basics.basics;

public class Memory02ThreadStack {

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            int x = 10;
            System.out.println("Thread t1"+x);
        });

        Thread t2 = new Thread(()->{
            int x = 10;
            System.out.println("Thread t2"+x);
        });

        t1.start();
        t2.start();
    }
}
