package com.basics.conc01executors.conc02execbasics;

public class Pro01_RwaThreadProblem {
    static int taskCount = 0;

    static void main()throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread [] threads = new Thread[10];

        for(int i=0;i<10;i++){
            final int taskId = i;

            threads[i] = new Thread(()->{
                System.out.println("Task id :"+taskId+" running on "+Thread.currentThread().getName());

                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                synchronized (Pro01_RwaThreadProblem.class){
                    taskCount++;
                }
            });

            threads[i].start();
        }

    }
}
