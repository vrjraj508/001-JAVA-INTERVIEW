package com.basics.basics;

import java.util.Arrays;
import java.util.Scanner;

class Arr001ReadData{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of servers:");
        int n = scanner.nextInt();

        int arr[] = new int[n];

        for(int i=0;i<arr.length;i++){
            System.out.println("Enter the numbers please: ");
            arr[i] = scanner.nextInt();
        }

        System.out.println(Arrays.toString(arr));
    }

}

class Arr01CountElements{
    public static void main(String[] args) {
        String [] deployments = {
                "dep_001",
                "dep_002",
                "dep_003",
                "dep_004"
        };

        int arr [] = {34,57,87,12,98,32,13};


        int[] dailyProfitLoss = {
                1200,
                -500,
                800,
                -200,
                1500,
                700
        };

        int profit = 0;
        int loss  = 0;

        for(int i=0;i<dailyProfitLoss.length;i++){
            if(dailyProfitLoss[i]>0){
                profit++;
            }else{
                loss++;
            }
        }

        System.out.println(profit+" "+loss);
    }
}

class Arr01FindRange{
    public static void main(String[] args) {

        int[] responseTimes = {
                120,
                85,
                210,
                65,
                150,78,90,123
        };

        int minResponseTime = responseTimes[0];
        int maxResponseTime = responseTimes[0];

        for(int i=0;i<responseTimes.length;i++){

            if(responseTimes[i] <minResponseTime){
                minResponseTime = responseTimes[i];
            }

            if(responseTimes[i] >maxResponseTime){
                maxResponseTime = responseTimes[i];
            }
        }

        int range = maxResponseTime-minResponseTime;

        System.out.println("Minimum: "+minResponseTime);
        System.out.println("Maximum: "+maxResponseTime);

        System.out.println("Range: "+range);

    }
}

class Array01EmptyChecking{
    public static void main(String[] args) {
        int [] transactionRecords = {};

        if(transactionRecords.length ==0){
            System.out.println("No transactions records available");
        }else{
            System.out.println("Transaction records found :"+transactionRecords.length);
        }
    }
}

class  Array01CopyArray{

public static void main(String[] args) {
    int [] originalConfig = {
            100,
            200,
            300,
            400,
            500,
            600
    };

    int[] copiedConfig = new int[originalConfig.length];

    for(int i=0;i<originalConfig.length;i++){
        copiedConfig[i] = originalConfig[i];
    }

    System.out.println(Arrays.toString(originalConfig));
    System.out.println(Arrays.toString(copiedConfig));
}
        }




public class Array01Concepts {

}
