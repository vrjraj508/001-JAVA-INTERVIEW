package com.basics.basics.das02arrays;

import java.util.Arrays;

public class Array01Basics {

    public static <T> void showArray(T [] arr){
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }

    }
    public static void showArray(int [] arr){
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }

        System.out.println();
    }

    public static void twoSum(){

    }

    public static int [] twoSumProblem(int [] arr,int target){

        for(int i=0;i<arr.length;i++){
            for(int j=i+1;j<arr.length;j++){
                if(arr[i]+arr[j]==target){
                    return new int[] {i,j};

                }
            }
        }

        return new int[]{-1};

    }

    public static int [] learn01Poiniters(int [] arr){
        if(arr==null || arr.length <2){
            return new int[0];
        }

        int[] result = new int[arr.length -1];

        for(int i=0;i<arr.length-1;i++){
            result[i] = arr[i]+ arr[i+1];
        }

        return result;
    }

    public static int [] learn01AllSums(int [] arr){
        int n = arr.length;

        int size  = n*(n-1)/2;
        int [] result = new int[size];

        int index = 0;

        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                result[index++] = arr[i]+arr[j];
            }

        }
        return result;

    }

    public static void main(String[] args) {

        int [] nums = {1,4,5,2,34,66,21,1,55,98,32,24,28};

        showArray(learn01Poiniters(nums));

        showArray(learn01AllSums(nums));

        showArray(twoSumProblem(nums,22));





    }
}
