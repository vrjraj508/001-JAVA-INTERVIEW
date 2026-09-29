package com.basics.conc01executors.conc01basics;

public class Thread02LifeCycle {

    static void main() throws InterruptedException {
        Object lock = new Object();

        Thread t1 = new Thread(()->{
            synchronized (lock) {
                System.out.println("[t1] acquired lock and sleeting in 3s");
                try{
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        },"t1-lock-holder");

    Thread t2 = new Thread(()->{
        System.out.println("[t2] Trying to lock ...");
        synchronized (lock){
            System.out.println("[t2] got the lock ...");

        }
    },"t2-blocked");

    Thread t3 = new Thread(()->{
        System.out.println("[t3] About to wait ...");
        synchronized (lock){
            try{
                System.out.println("[t3]Wait done ...");

                    lock.wait(2000);

            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    },"t3-waiter");

        System.out.println("t1 after new:"+t1.getState());

        t1.start();
        Thread.sleep(200);
        System.out.println("t1 while sleeping: "+t1.getState());

        t2.start();
        Thread.sleep(100);
        System.out.println("t2 while blocked:"+t2.getState());

        t3.start();
        Thread.sleep(100);
        System.out.println("t3 state: " + t3.getState());

        t1.join();
        t2.join();
        t3.join();

        System.out.println("t1 after join: " + t1.getState());
        System.out.println("t2 after join: " + t2.getState());
        System.out.println("t3 after join: " + t3.getState());



    }
}
