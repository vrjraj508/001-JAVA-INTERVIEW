package com.basics.easy;

public class Arrays01SlidingWindow {

    public static int maxSum(int [] arr, int k){

        return 0;
    }

    public static void allSubArrays(int [] arr,int window){
        for(int start = 0;start <arr.length;start++){
            for(int end = start;end <arr.length;end++){
                for(int k = start;k<=end;k++){
                    System.out.print(arr[k]);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static void allSubArraysOfSizeK(int arr[],int k){
        int maxSum = Integer.MIN_VALUE;

        for(int i=0;i<arr.length-k;i++){
            int currSum = 0;

            for(int j = i;j<i+k ;j++){
                currSum += arr[j];
            }

            maxSum = Math.max(maxSum,currSum);
        }
        System.out.println(maxSum);
    }


    public static void main(String[] args) {
        int [] arr = {2,1,5,1,3,2};

        //allSubArrays(arr,3);
        allSubArraysOfSizeK(arr,3);

    }
}
