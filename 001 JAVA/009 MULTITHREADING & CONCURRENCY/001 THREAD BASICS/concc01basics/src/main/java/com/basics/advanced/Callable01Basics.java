package com.basics.advanced;

import javax.print.attribute.standard.PresentationDirection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CustomerProfileTask{
    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Callable<String> customerTask = ()->{
            System.out.println(
                    "Fetching customer profiles on: "+ Thread.currentThread().getName()
            );
            Thread.sleep(2000);
            return "Customer Profile restured Successfully";
        };

        var future = executorService.submit(customerTask);

        String res = future.get();

        System.out.println("Main Thread continues...");
        System.out.println("Result:"+ res );

        executorService.shutdown();

    }
}

public class Callable01Basics {
}
