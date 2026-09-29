package com.basics.conc01executors.conc01basics;

class Utils {

    public static void doJob001() throws InterruptedException {
        System.out.println();
        Thread.sleep(50);
        for(int i=0;i<100;i++){
            System.out.print("*");
        }
        System.out.println("Printed stars till 100");
    }


    public static void doJob002() throws InterruptedException {
        System.out.println();
        Thread.sleep(50);
        for(int i=0;i<100;i++){
            System.out.print("=");
        }
        System.out.println("Printed equals sign till 100");
    }

    public static void printLetters() throws InterruptedException {
        for(int ch = 65;ch<=90;ch++){
            Thread.sleep(50);
            System.out.print((char) ch+" ");
        }


    }

        }

class Worker extends Thread {
    @Override
    public void run(){
        System.out.println("Worker Method started");

        try{
            Thread.sleep(3000);

        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Worker Task is finished");
    }


}

class DownloadTask extends  Thread{
    private String fileName;

    public DownloadTask(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void  run(){
        System.out.println("File: "+fileName+" Downloading started");

        try{
            Thread.sleep(2000);

        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Download Completed");
    }
}

class Sumtask extends  Thread{
    private long start;
    private long end;

    private long result;

    public Sumtask(long start,long end){
        this.start = start;
        this.end = end;
    }

    @Override
    public void run(){
        for(long i = start;i<=end;i++){
            result += 1;
        }
    }

    public long getResult(){
        return result;
    }
}

public class Thread02Join {

    static void main() throws InterruptedException {
        Sumtask t1 = new Sumtask(1,50_000_000);
        Sumtask t2 = new Sumtask(50_000_001,100_000_000);

        t1.start();
        t2.start();

        /*t1.join();
        t2.join();*/

        long finalSum = t1.getResult() + t2.getResult();

        System.out.println("Final num is: "+finalSum);


    }

}
