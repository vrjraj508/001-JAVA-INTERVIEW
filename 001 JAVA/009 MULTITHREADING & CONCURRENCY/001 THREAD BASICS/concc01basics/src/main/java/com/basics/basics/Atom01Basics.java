package com.basics.basics;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Program01RequestCounter{
    private static final int NUMBER_OF_THREADS = 10;
    private static final int REQUESTS_PER_THREAD = 1_000;

    private static final AtomicInteger totalRequests = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        for(int i=1;i<=NUMBER_OF_THREADS;i++){
            int workerId = i;

            executorService.submit(()->{
                for(int request = 1;request <=REQUESTS_PER_THREAD;request++){
                    processRequest(workerId);

                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(
                1,
                TimeUnit.MINUTES
        );

        System.out.println(NUMBER_OF_THREADS*REQUESTS_PER_THREAD);
        System.out.println(totalRequests.get());
    }

    private static void processRequest(int workerId){
        totalRequests.incrementAndGet();
    }
}

class Program01OrderCounter{
    private static final int NUMBER_OF_WORKERS = 5;
    private static final int ORDERS_PER_WORKER = 1_000;

    private static final AtomicInteger ordersReceived = new AtomicInteger(0);
    private static final AtomicInteger ordersProcessed = new AtomicInteger(0);
    private static final AtomicInteger ordersFailed = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_WORKERS);

        for(int i=1;i<=NUMBER_OF_WORKERS;i++){
            int workerId = i;

            executorService.submit(()->{

                for(int order = 1;
                 order <=ORDERS_PER_WORKER;
                 order++
                ){
                    processOrder(workerId,order);

                }

            });
        }

        executorService.shutdown();
        executorService.awaitTermination(
                1,
                TimeUnit.MINUTES
        );

        printResult();



    }

    private static void processOrder(int workerId, int orderNumber) {
        int orderNumberInSystem = ordersReceived.incrementAndGet();

        try{
            if(orderNumber % 20 ==0){
                throw new RuntimeException(
                        "Simulated Order Failure"
                );
            }

            ordersProcessed.incrementAndGet();
        }catch (Exception ex){
            ordersFailed.incrementAndGet();
        }
    }

    public static void printResult(){
        System.out.println("**********Get Results************");
        System.out.println("Order receivced: "+ordersReceived.get());
        System.out.println("Order Processed: "+ordersProcessed.get());
        System.out.println("Order Failed: "+ordersFailed.get());
        System.out.println("Processed with Failed: "+(ordersProcessed.get()+ordersFailed.get()));
    }
}

class Program03ProgramIdGenerator{
    private static final int STARTING_ID = 1000;
    private static final int NUMBER_OF_THREADS = 10;
    private static final int EMPLOYEES_PER_THREAD = 10;
    private static final AtomicInteger employeeIdGenerator =
            new AtomicInteger(STARTING_ID);

    private static final Set<Integer> generatedIds = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {

    }

    private static void createEmployee(int workerId) {
    }

}

public class Atom01Basics {
}
