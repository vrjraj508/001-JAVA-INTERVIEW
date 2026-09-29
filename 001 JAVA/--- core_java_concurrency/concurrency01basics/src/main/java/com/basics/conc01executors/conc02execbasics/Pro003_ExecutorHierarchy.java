package com.basics.conc01executors.conc02execbasics;

import java.util.concurrent.*;

public class Pro003_ExecutorHierarchy {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        ExecutorService poolBasics = Executors.newFixedThreadPool(2);

        Future<Integer> future = pool.submit(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return 99;
        });

        System.out.println("Waiting ...");
        Integer result = future.get();

        System.out.println(result);


    }
}
