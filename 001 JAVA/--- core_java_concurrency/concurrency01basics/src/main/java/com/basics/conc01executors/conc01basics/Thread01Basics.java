package com.basics.conc01executors.conc01basics;

class MyThread implements  Runnable{

    @Override
    public void run() {
        System.out.println("My thread is running with a runnable thread as we can see here:"+Thread.currentThread().getName());
    }
}

class WhatIsAThread{
    static void main() throws InterruptedException {

        Thread main = Thread.currentThread();
        System.out.println(main.getName());
        System.out.println(main.threadId());
        System.out.println(main.getPriority());
        System.out.println(main.isDaemon());
        System.out.println(main.getState());

        Thread th1 = new Thread(()->{
            System.out.println("New thread:"+ Thread.currentThread().getName());
            System.out.println(Thread.currentThread().threadId());
            System.out.println(Thread.currentThread().getState());
        });
        th1.setName("worker-thread-001");
        th1.start();

        System.out.println("main state of T1 after start()"+ th1.getState());
        th1.join();


    }
}

class CreatingThreads{
    static class MyThread extends Thread{
        private final String taskName;

        MyThread(String taskName){
            this.taskName = taskName;
            setName("Thread-"+taskName);
        }

        @Override
        public void run(){
            System.out.println("<Way 001>"+taskName+"on thread"+Thread.currentThread().getName());
        }
    }

    static class MyRunnable implements Runnable{
        private final String taskName;

        MyRunnable(String taskName){this.taskName = taskName;}

        @Override
        public void run(){
            System.out.println("<Way 2>"+taskName+" on thread "+Thread.currentThread().getName());
        }

    }

    public static void implementLongTask() throws InterruptedException {
        System.out.println("Loading....");
      for(int i=0;i<100;i++){
          System.out.print("*");
          Thread.sleep(100);
      }
        System.out.println();
        System.out.println("Task Completed");

    }

    static void main() throws InterruptedException {
        MyThread t1 = new MyThread("taskA");
        t1.start();

        Thread t2 = new Thread(new MyRunnable("taskB"));
        t2.setName("taskB-Implements runnable");
        t2.start();

        Thread t3 = new Thread(()->{

            System.out.println("Implementing task C on"+Thread.currentThread().getName());
            try {
                implementLongTask();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("<Main> All the threads are completed");
    }
}

public class Thread01Basics extends Thread{

    public void run(){
        System.out.println("Running the thread in :"+Thread.currentThread().getName());

    }

  public  static void main() {

        Thread01Basics th01 = new Thread01Basics();
        Thread01Basics th02 = new Thread01Basics();

        th01.run();
        th02.start();
    }


}
