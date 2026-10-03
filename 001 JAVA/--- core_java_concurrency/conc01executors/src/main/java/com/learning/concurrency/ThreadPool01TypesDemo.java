package com.learning.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool01TypesDemo {
    static AtomicInteger taskIdGenerator = new AtomicInteger(0);

    static Runnable createTask(String poolName,int durationMs){
        int taskId = taskIdGenerator.incrementAndGet();

        return ()->{
            String thread = Thread.currentThread().getName();
            System.out.println("Thread pool name"+poolName+" Task --"+taskId+"started on "+thread);

            try{
                Thread.sleep(durationMs);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println(poolName+" Task ---"+taskId+" interrupted on "+thread);
                return;
            }
            System.out.println(poolName+" Task ---"+taskId+" finished on"+thread);
        };
    }

    static void demonstrateFixedPool() throws InterruptedException{
        System.out.println("******** Fixed Thread Pool (3 Threads 7 tasks)********");
        ExecutorService fixed = Executors.newFixedThreadPool(3);

        for(int i=0;i<7;i++){
            fixed.execute(createTask("FIXED",500));
        }

        fixed.shutdown();
        boolean done = fixed.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Fixed Poo Terminated"+done);
    }

    public static void main(String[] args) throws InterruptedException {
        demonstrateFixedPool();
    }
}
