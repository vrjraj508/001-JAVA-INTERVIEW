package com.basics.conc01executors.conc02execbasics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pro002_ExecutorSolutions {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        ExecutorService executors = Executors.newFixedThreadPool(3);

        for(int i=0;i<10;i++){
            final int taskId = i;

            executors.submit(()->{
                System.out.println("Task :"+taskId+"-->"+ Thread.currentThread().getName());

                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            });
        }

        executors.shutdown();
        executors.awaitTermination(5, TimeUnit.SECONDS);

        long elapsed = System.currentTimeMillis() -start;

        System.out.println("All tasks done in:"+elapsed+" ms");
        System.out.println("Only three threads were ever created and Pool reused them");

    }
}
