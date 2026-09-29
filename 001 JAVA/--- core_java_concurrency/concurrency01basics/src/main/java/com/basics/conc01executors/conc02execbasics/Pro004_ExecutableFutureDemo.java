package com.basics.conc01executors.conc02execbasics;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Pro004_ExecutableFutureDemo {

    public static void exec01FutureExample() throws ExecutionException , InterruptedException{
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("-----Cached Thread poo --------");
        for(int i=1;i<=10;i++){
            final int id = 1;
            executor.execute(()->{
                System.out.println("Do this task with id:"+id+" on "+Thread.currentThread().getName());
            });


        }

        executor.shutdown();

        ExecutorService fixed = Executors.newFixedThreadPool(2);

        System.out.println("-----------Cached Thread Pool--------------");
        for(int i=1;i<=5;i++){
            final int id = i;

            fixed.execute(()->{
                System.out.println("Fixed Task:"+id+" on"+Thread.currentThread().getName());
            });

        }

        fixed.shutdown();

    }

    public static void exec02SingleThreadExec(){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for(int i=0;i<=5;i++){
            final int taskId = i;
            executor.execute(()->{
                System.out.println("Task"+taskId+" by "+Thread.currentThread().getName());
            });

        }

        executor.shutdown();
    }

    public static void exec003ScheduledExecutorService(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        scheduler.schedule(()-> System.out.println("One shot task:"),2,TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(()-> System.out.println("Periodic Task @"+System.currentTimeMillis()),1,3,TimeUnit.SECONDS);

        scheduler.schedule(()->{
            System.out.println("Shutting down scheduler");
            scheduler.shutdown();
        },10,TimeUnit.SECONDS);
    }

    public static void exec004SingleThread(){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for(int i=0;i<=5;i++){
            final int taskId = i;
            executor.execute(()->{
                System.out.println("Task :"+taskId+"by "+Thread.currentThread().getName());
            });
        }

        executor.shutdown();
    }

    public static void exec005ScheduledExecutorService(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        scheduler.schedule(()-> System.out.println("One shot task"),2,TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(()-> System.out.println("Periodic Tasks"),1,3,TimeUnit.SECONDS);

        scheduler.schedule(()->{
            System.out.println("Shutting Down scheduler ....");
        },10,TimeUnit.SECONDS);
    }

    public static void exec006BatchSubmission() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Callable<String>> tasks = Arrays.asList(
                ()->{Thread.sleep(500); return "Task A";},
                ()->{Thread.sleep(200); return "Task B";},
                ()->{Thread.sleep(1000); return  "Task C";}
        );

        List<Future<String>> futures = executor.invokeAll(tasks);
        System.out.println("All tasks are done ");
        for(Future<String> f:futures){
            System.out.println(f.get());
        }

        String firstResult = executor.invokeAny(tasks);

        System.out.println("First Completed:"+firstResult);

        executor.shutdown();

    }

    public static void exec007GracefulShutdown(){
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try{
            Future<?> future = executor.submit(()->{
                throw new RuntimeException("Boom !!");
            });

            future.get();

        } catch (ExecutionException e) {
            System.out.println("Caught execution from task"+e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }finally {
            executor.shutdown();

            try{
                if(!executor.awaitTermination(5,TimeUnit.SECONDS)){
                    executor.shutdown();
                    System.out.println("Forced Shutdown");
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }

    //Main Method
    public static void main(String[] args) throws ExecutionException, InterruptedException {
     exec007GracefulShutdown();

    }
}
